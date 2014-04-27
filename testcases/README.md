# Typechecking tests for Compilers

For the past few years, Rick Ord's CSE 131 has had a student-run repository of shared test cases. It's worked pretty well, so let's do it again.

## How it works

The compiler spec requires a specific output for specific types of input. When your compiler is graded, they'll feed it awful broken source code and compare the results to the output they expect. Using this repo and a [testing framework](https://github.com/bulatb/yuno), we can do the same.

Each test case in the repo has two parts: a source file (testname.rc) to be fed to the compiler, and an answer file (testname.ans.out) that contains the output to expect. The framework will compare your outputs to the answers and alert you if something is wrong. The more people who contribute tests, the better everyone can do. And the class isn't curved, so why not?

## Versioning and sharing tests

I'm leaning toward the standard fork/clone/pull-request model where everybody forks the parent repo, clones their fork, and issues pull requests upstream when they have something to share. On one hand it's a useful thing to learn and makes sure there's a gatekeeper to stop bad things like conflicts and errors, but I'm wondering if it would add a lot of friction to the process over, for example, just giving everyone commit rights and hoping chaos doesn't reign. (It's been a problem in the past.)

I'll post a draft of the setup instructions for that model in the `readme-draft` branch when I've finished writing it.

If you have opinions or suggestions, please post in the Piazza thread and tell me what you think.


# Important: You can use this without Git

Whatever option we decide to use, you can always get a snapshot of the repo as a Zip file and use GitHub's online editor to share your tests. The goal is that this testing system works for everyone.
