## Get started
Start by cloning the repository and create a local development branch
different from the `master` branch.

```bash
git clone ssh://git@git.kroon.fi:10022/creavisio/immo-pdf-android.git
```

Keep your local `master` branch deployable at all times aka clean. Dont develop locally on the master branch.

### Branch out
Working on a `feature` or a `bugfix`? create a branch of that like:

```bash
git branch bugfix/issue51
```

Where issue number 51 is the corresponding `#51` issue number on gitea.

**Dont forget to checkout that newly created branch**

Then work on your feature or issue and push those branch changes
to a remote branch with the same name as your local branch.

**All feature code must have accompanying tests**

## Developer
Once you have completed your work, you create a pull request on gitea and wait
for a reviewer to respond.

## Reviewer
 A merge into master is done by a reviewer locally -- the **pull requests on gitea instance are never merged via the web gui** -- its simply just a way for us to discuss the changes and view the changes easily because all changes are signed by gpg.

Merging into master is done by pulling the latest master branch and the remote feature branch and simply merge it into master locally and thus signing the commit
and push it to the remote master branch.

In commands:
```bash
git pull origin master
git fetch
git merge bugfix/issue51
git push origin master
```

Then close the pull request on gitea.
