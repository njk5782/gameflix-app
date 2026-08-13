let currentToken = sessionStorage.getItem("gameflixToken");

async function sendRequest(url, options = {}) {
    const response = await fetch(url, {
        ...options,
        headers: {
            "Content-Type": "application/json",
            ...options.headers
        }
    });

    const responseBody = await response.json();

    if (!response.ok) {
        throw new Error(responseBody.message || "Request failed");
    }

    return responseBody;
}

function showMessage(form, text, successful = false) {
    const output = form.querySelector("output");
    output.textContent = text;
    output.className = successful ? "ok" : "";
}

function setUpAccountForm(formSelector, endpoint) {
    const form = document.querySelector(formSelector);

    form.addEventListener("submit", async function (event) {
        event.preventDefault();

        const formData = new FormData(form);
        const account = Object.fromEntries(formData);

        try {
            const result = await sendRequest(endpoint, {
                method: "POST",
                body: JSON.stringify(account)
            });

            const successful = result.message.includes("success");
            showMessage(form, result.message, successful);

            if (successful) {
                if (result.token) {
                    currentToken = result.token;
                    sessionStorage.setItem("gameflixToken", result.token);
                    await loadSubscription();
                    showMessage(
                        form,
                        "Login successful",
                        true
                    );
                }
                form.reset();
            }
        } catch (error) {
            showMessage(form, error.message);
        }
    });
}

function escapeHtml(value) {
    const specialCharacters = {
        "&": "&amp;",
        "<": "&lt;",
        ">": "&gt;",
        "'": "&#39;",
        '"': "&quot;"
    };

    return String(value).replace(
        /[&<>'"]/g,
        character => specialCharacters[character]
    );
}

async function loadGames() {
    const gameList = document.querySelector("#movies");

    try {
        const games = await sendRequest("/api/movies");

        if (games.length === 0) {
            gameList.innerHTML =
                '<p class="empty">No games yet. Add the first title.</p>';
            return;
        }

        gameList.innerHTML = games.map(game => `
            <article class="card">
                <p>${escapeHtml(game.genre)}</p>
                <h3>${escapeHtml(game.title)}</h3>
                <p>Released ${game.releaseYear}</p>
                <button
                    class="remove-game"
                    data-game-id="${game.id}"
                    data-game-title="${escapeHtml(game.title)}"
                    type="button"
                >
                    Remove
                </button>
            </article>
        `).join("");
    } catch (error) {
        gameList.innerHTML =
            `<p class="empty">${escapeHtml(error.message)}</p>`;
    }
}

setUpAccountForm("#register", "/api/register");
setUpAccountForm("#login", "/api/login");

const gameForm = document.querySelector("#movie");
const gameList = document.querySelector("#movies");

gameList.addEventListener("click", async function (event) {
    const removeButton = event.target.closest(".remove-game");

    if (!removeButton) {
        return;
    }

    const gameId = removeButton.dataset.gameId;
    const gameTitle = removeButton.dataset.gameTitle;

    try {
        const result = await sendRequest("/api/movies/" + gameId, {
            method: "DELETE",
            headers: getAuthorizationHeader()
        });

        showMessage(gameForm, result.message + ": " + gameTitle, true);
        await loadGames();
    } catch (error) {
        showMessage(gameForm, error.message);
    }
});

gameForm.addEventListener("submit", async function (event) {
    event.preventDefault();

    const game = Object.fromEntries(new FormData(gameForm));
    game.releaseYear = Number(game.releaseYear);

    try {
        const savedGame = await sendRequest("/api/movies", {
            method: "POST",
            headers: getAuthorizationHeader(),
            body: JSON.stringify(game)
        });

        showMessage(
            gameForm,
            `${savedGame.title} added to the game catalog`,
            true
        );

        gameForm.reset();
        await loadGames();
    } catch (error) {
        showMessage(gameForm, error.message);
    }
});

const planButtons = document.querySelectorAll(".plan");

planButtons.forEach(function (button) {
    button.addEventListener("click", async function () {
        const output = document.querySelector("#plan-message");

        try {
            const result = await sendRequest("/api/subscription", {
                method: "PUT",
                headers: getAuthorizationHeader(),
                body: JSON.stringify({
                    plan: button.dataset.plan
                })
            });

            selectPlan(result.plan);
            output.textContent = result.plan + " plan saved";
            output.className = "ok";
        } catch (error) {
            output.textContent = error.message;
            output.className = "";
        }
    });
});

loadGames();

function getAuthorizationHeader() {
    if (!currentToken) {
        return {};
    }

    return {
        "Authorization": "Bearer " + currentToken
    };
}

async function loadSubscription() {
    if (!currentToken) {
        return;
    }

    try {
        const result = await sendRequest("/api/subscription", {
            headers: getAuthorizationHeader()
        });

        selectPlan(result.plan);
    } catch (error) {
        const output = document.querySelector("#plan-message");
        output.textContent = error.message;
        output.className = "";
    }
}

function selectPlan(planName) {
    planButtons.forEach(function (plan) {
        const selected =
            planName &&
            plan.dataset.plan.toUpperCase() === planName.toUpperCase();
        plan.classList.toggle("selected", selected);
    });
}

loadSubscription();
