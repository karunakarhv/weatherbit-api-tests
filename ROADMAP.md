# Framework Roadmap — Weather API Test Automation

## Current State (v1.0)
- Serenity BDD + Cucumber 7 + REST Assured
- 4 feature files covering all 4 acceptance criteria
- Metadata-driven AC4 via `us_states.json`
- Serenity HTML report generated on every run
- JMeter performance testing approach documented

---

## Phase 2 — Hardening & CI/CD

| Item                    | Value                                                      |
|-------------------------|------------------------------------------------------------|
| GitHub Actions pipeline | Run tests on every PR; publish Serenity report as artifact |
| JSON Schema validation  | Assert response structure with `json-schema-validator`     |
| Secret management       | Store API key in GitHub Secrets / AWS Parameter Store      |

---

## Phase 3 — Extended Coverage

| Item                            | Value                                            |
|---------------------------------|--------------------------------------------------|
| Negative / error scenarios      | 401 invalid key, 400 bad city, rate-limit 429    |
| Forecast endpoint (`/forecast`) | Expand beyond `/current`                         |
| Data-driven expansions          | Externalise all city/coordinate data to JSON/CSV |

---

## Phase 4 — Observability & Reporting

| Item                 | Value                                                   |
|----------------------|---------------------------------------------------------|
| Allure integration   | Richer report with timeline, categories, trends         |
| TestRail / Xray sync | Push results to test management system                  |
| Slack notifications  | Post pass/fail summary to team channel on CI completion |

---

