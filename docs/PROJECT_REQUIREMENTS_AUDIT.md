# GameFlix Project Requirements Audit

Audit date: August 12, 2026

This matrix separates evidence found in the repository from assignments that
may already have been submitted through the course site or retained in earlier
chat conversations. `Verify prior submission` does not mean the work was not
completed; it means the artifact was not found in this repository or the local
Codex output search.

| Requirement | Status | Evidence or next action |
| --- | --- | --- |
| Prompt journal | Complete | `PROMPT_JOURNAL.md` records significant prompts, tools, changes, reasoning, and verification. |
| AI attribution | Prepared | `docs/AI_ATTRIBUTION.md` documents assistance. Use `[AI-assisted]` in the next substantial code commit. |
| Explain, test, and critique AI-generated components | In progress | Tests and `docs/CODE_AUDIT.md` provide evidence. The final video still needs the required live explanation and modification. |
| Module 1: SRS | Verify prior submission | No SRS file was found in the repository. Confirm it exists in the course submission or retained chat. |
| Module 1: five user stories with acceptance criteria | Verify prior submission | No user-story document was found in the repository. |
| Module 1: three use-case diagrams | Verify prior submission | No diagram files were found in the repository. |
| Module 2: sprint plan with AI-adjusted estimates | Verify prior submission | No sprint-plan file was found in the repository. |
| Module 2: GitHub Project Board screenshot | Verify prior submission | No board screenshot was found locally. The board may still exist on GitHub or in the course submission. |
| Module 3: architecture diagram | Verify prior submission | No architecture diagram was found in the repository. |
| Module 3: at least three microservices and endpoints | Verify prior submission | The current prototype is one Spring Boot service with logical controller/service layers. Confirm the earlier design deliverable was submitted separately. |
| Module 4: registration and login APIs | Complete | `/api/register` and `/api/login`, BCrypt password hashing, frontend integration, and tests are present. |
| Module 4: 1–2 page generated-code audit | Complete | `docs/CODE_AUDIT.md` identifies six issues, corrections, limitations, and future improvements. |
| Module 4: protected JWT route and Postman testing | Complete | JWT interceptor, protected subscription/catalog changes, and the Postman collection are present. |
| Module 4: two failed-auth attack cases | Complete | `docs/AUTH_ATTACK_LOG.md` and Postman assertions cover missing and invalid tokens. |
| Module 5: Dockerfile | Complete | `Dockerfile` packages the JAR with a Java 17 runtime. |
| Module 5: explain every Dockerfile directive | Complete | `docs/DOCKERFILE_EXPLAINED.md` explains `FROM`, `WORKDIR`, `COPY`, and `ENTRYPOINT`. |
| Module 5: GitHub Actions CI with automated tests | Complete locally; push required | The workflow builds with Maven, which runs all 13 tests. The current changes must be committed and pushed for GitHub to run them. |
| Module 5: automated security scan | Complete locally; full CI run pending | OWASP Dependency-Check and report uploads are configured and documented. Confirm the complete GitHub Actions result after pushing. |
| Module 6: deployed application and working link | Not complete | The application works locally with Docker, but a public deployment link has not been confirmed. |
| Module 6: deployment documentation | Prepared; URL pending | `docs/DEPLOYMENT.md` documents the Railway setup. Add the generated URL and final verification results after deployment. |
| Module 6: 10-minute demo | Draft is outdated | The retained demo guide predates JWT, stored subscriptions, game removal, Postman testing, 13 tests, and the required two-minute live code modification. Rewrite after deployment decisions. |
| Module 6: individual reflection | Verify prior draft; final copy needed | No final reflection file was found in this repository. Confirm any earlier course submission, then update it to cover AI help and misleading output. |

## Confirmed remaining work

1. Check the course site or retained chats for the Module 1–3 artifacts and the
   reflection instead of recreating already submitted work unnecessarily.
2. Commit and push the current project with an `[AI-assisted]` message, then
   confirm the GitHub Actions build and security scan.
3. Deploy GameFlix and record the working public URL and deployment process.
4. Update the final demo guide to reflect the current features, include the
   required two-minute live code walkthrough/modification, and mention the real
   test totals and limitations.
