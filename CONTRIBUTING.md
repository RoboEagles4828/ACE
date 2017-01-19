# How to Contribute to this repository (unfinished)

## Guidelines

This year, we are using **git flow**, a development model to make our repository all neat and pretty!

The main idea is to use separate branches for development and for features. The full blog post can be found [here](blogpost).

### So... how do you use git flow?

#### Installation

##### The easy way

- Install the git flow extension.
    - Follow [these](instructions) instructions.
    
That's it! You're done! Well... ALMOST done. Now you just need to know how to use git flow. A cheat sheet can be found [here](cheatsheet)

##### The "do it yourself" way

The blog gives a good explanation on how to manually use commands to use the git flow technique. Although this is only recommended for people experienced in git, you can try it if you want. Just don't delete all our code. 

## How to add code

So let's say you have a great idea for the robot, such as better code for the shooter or something. How would you put this code on GitHub?

DISCLAIMER: Again, this guide assumes that you have prior git knowledge. PLEASE learn git before you attempt this. You WILL break things if you don't.

### 1. Get the code from GitHub

If you haven't already, clone the repository into a folder:

`git clone https://github.com/RoboEagles4828/2017Robot.git`

Now `cd` into the repository.

### 1.5 Set up `git flow`

If you are using the git flow extension, set it up using:

`git flow init`

### 2. Create a new branch off of `develop`

Since all features must branch off develop, make a branch from develop:

With extension: `git flow feature start <name>`

Without extension: `git checkout -b <name> develop`

### 3. Add your code!

Type up your code in whatever text editor you choose, and commit using:

`git commit -a -m "Title message" -m "Description (optional)"`

### 4. Connect your local branch to remote and push your code

Now, connect your branch to the remote and push your commits:

`git push --set-upstream origin/<name> <name>`

### 5. Make a pull request back into `develop`

**EXTENSION USERS: This is EXTREMELY important! DO NOT use `git flow feature finish <name>`! Since our branches have protection, this will break things, most likely deleting your code!**

Go to GitHub. On the repository page, you should see a tab labeled "Pull Requests". Click it.

On the right hand side, click "New pull request".

Near the top of the page, there should be 2 dropdowns, each with `master` selected. Switch the base one to `develop` and the compare one to your branch. Click "Create Pull Request".

Create a nice title for yout pull request, and click "Create pull request".

### 6. Wait for review

Near the bottom, you will see some checks. You must wait for these to finish before merging.

You will also need a person to review your code. Just contact any programmer on Slack, and they'll probably look it over for you and merge it.

### 7. See your code on the robot!

Now that the code is in develop, it is officially a part of the robot code! 

## Issues and not knowing what to do at the shop

We've all experience this before. You get to the shop, get out your computer, open up IntelliJ (or, god forbid, Eclipse), and sit there for 30 minutes, not knowing what to do.

Say no more! Introducing, Issues and Projects!

Issues are basically problems with the code, and Projects are a place to store all the TODO's and such.

### Making an Issue

Found a bug in the code? Want a feature to be implemented on the robot? Here's how to make an Issue.

1. Click the "Issues" tab on the toolbar
2. Click "New Issue"
3. Type the Issue and click "Submit new issue"
4. On the right side, assign people to the Issue

That's it for making an Issue!

#### Good practices for Issue-making

- Try to use an insightful title
    - Good: "Problem with vision when running with toaster mode on" or "Pancake cooking functionality?"
    - Not good: "The robot broke" or "I want something to be added"
- Include exactly what happened if the Issue is about a bug. Try to be as specific as possible.
- Don't use Issues for random information. See [here](ripnikhil) for an example.
- When referencing other Commits, Pull Requests, or Issues, use it's `#` code. This can be found next to the name of the commit/issue/pull request. Example "Jackie, you totally broke the whole robot in #123"

### Using Projects

Projects can be found at the GitHub organization page or the repository page in the "Projects" tab. This is basically a running list of things to do.

If you want to do a thing, click the dropdown arrow and then "Convert to Issue". This allows you to make an Issue for that card! When finished, drag to the appropriate section.

[blogpost]: http://nvie.com/posts/a-successful-git-branching-model/
[instructions]: https://github.com/nvie/gitflow/wiki/Installation
[cheatsheet]: http://danielkummer.github.io/git-flow-cheatsheet/
[ripnikhil]: https://github.com/RoboEagles4828/2017Robot/issues/10