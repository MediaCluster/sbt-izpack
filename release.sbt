import sbtrelease._
import ReleaseStateTransformations._
import ReleaseKeys._
import sbtrelease.{Git, Utilities, ExtraReleaseCommands}
import Utilities._

lazy val runScriptedTests = TaskKey[Unit]("Run all scripted tests")

runScriptedTests := scripted.toTask("").value

lazy val runScripted: ReleaseStep = releaseStepTask(runScriptedTests)

def extractGitCmd: (State) => (Git) = { st: State =>
  st.extract.get(releaseVcs).get.asInstanceOf[Git]
}

def extractReleaseVersion: (State) => (String) = { st =>
  st.get(versions).map {_._1}.getOrElse(
    sys.error("Unable to get version from state, has release step inquireVersions already been run?")
  )
}

def checkReleaseBranchExistsAndMatchesVersion: (State) => (State) = { st: State =>
  val git = extractGitCmd(st)
  val version = extractReleaseVersion(st)
  val branch = (git.cmd("rev-parse", "--abbrev-ref", "HEAD") !!).trim
  if (!branch.equals(s"release-$version")) {
    sys.error(s"Branch does not match <release-$version>")
  }
  st
}

def mergeReleaseBranchIntoMasterAndSwitchBack: (State) => (State) = { st =>
  val git = extractGitCmd(st)
  val version = extractReleaseVersion(st)
  val branch = (git.cmd("rev-parse", "--abbrev-ref", "HEAD") !!).trim
  if (!branch.equals(s"release-$version")) {
    sys.error(s"Branch does not match <release-$version>")
  }
  git.cmd("checkout", "master") ! st.log
  git.cmd("pull", "origin", "master") ! st.log
  git.cmd("merge", branch, "--no-ff", "--no-edit") ! st.log
  git.cmd("checkout", branch) ! st.log
  st
}

def mergeReleaseBranchIntoDevelopAndStayThere: (State) => (State) = { st =>
  val git = extractGitCmd(st)
  val version = extractReleaseVersion(st)
  val branch = (git.cmd("rev-parse", "--abbrev-ref", "HEAD") !!).trim
  if (!branch.equals(s"release-$version")) {
    // just to be on the safe side
    sys.error(s"Branch does not match <release-$version>")
  }
  git.cmd("checkout", "develop") ! st.log
  git.cmd("pull", "origin", "develop") ! st.log
  git.cmd("merge", branch, "--no-ff", "--no-edit") ! st.log
  st
}

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  ReleaseStep({st => checkReleaseBranchExistsAndMatchesVersion(st)}),
  runClean,
  runTest,
  runScripted,
  setReleaseVersion,
  commitReleaseVersion,
  publishArtifacts,
  tagRelease,
  ReleaseStep({st => mergeReleaseBranchIntoMasterAndSwitchBack(st)}),
  ReleaseStep({st => mergeReleaseBranchIntoDevelopAndStayThere(st)}),
  setNextVersion,
  commitNextVersion
)
