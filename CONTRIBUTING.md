# Contributing to k8s-capstone-deploy

We appreciate your interest in contributing to **Project Name**! To maintain consistency and quality, please follow the guidelines outlined below.

## Getting Started

1. Fork the repository and clone it locally:
   ```
   git clone https://github.com/your-username/project-name.git
   ```

2. Create a new branch for your feature or fix:
   ```
   git checkout -b feature/your-feature-name
   ```

3. Make your changes, and be sure to test thoroughly.


## Commit Message Guidelines

We follow the **Conventional Commits** format for clarity and automated changelog generation:

```
<type>: <short summary>
```
### Types:
- **feat**: A new feature
- **fix**: A bug fix
- **docs**: Documentation only changes
- **style**: Code style changes (formatting, white-space, etc.)
- **refactor**: A code change that neither fixes a bug nor adds a feature
- **test**: Adding or correcting tests
- **chore**: Changes to the build process or auxiliary tools

### Examples:
- `feat: add user authentication`
- `fix: correct null reference in user model`
- `docs: update contributing guidelines`
- `style: fix indentation in main.js`

## Branching Strategy

Branches should follow this naming convention:

- `feature/<feature-name>` - for new features
- `fix/<issue-name>` - for bug fixes
- `hotfix/<critical-fix>` - for urgent fixes in production

### Examples:
- `feature/user-authentication`
- `fix/navbar-not-loading`
- `hotfix/payment-gateway-timeout`

Before opening a pull request, make sure your branch is up-to-date with `main`:
```bash
git checkout main
git pull origin main
git checkout <your-branch>
git merge main
```
