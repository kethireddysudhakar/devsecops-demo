# DevSecOps Demo Environment
This repository implements a demo of DevSecOps tools against some sample code bases. The goal being to evaluate these tools against each other useing the same baseline and also provide examples of usage. 

It, presently, contains the following tools to support the demo. 

* Gitlab(13.5.3-ee)
* Jenkins(2.249.3)
* SonarQube(8.4.2 Community Edition)

As much as possible, we have bootstrapped the tool stack to work together. But some **manual** configuration may be needed once the stack is running. Please see the Quick Start section for details. 

The repository includes some open source license code to use for the demo and evaluating different DevSecOps tools. See the sample_projects directory. 

## Todo's
**This demo is not yet complete!** 

Below is a list of todo's that need to be done before it is considered complete enough to be of use.

* The initial pipeline, there may come more, needs to have the stages/steps filled out and run against the sample applications. Right now it it just a mock.

* This **README** needs to be finalized and all manual configuration included.
    * Update the gitlab token in gitlab and jenkins. Maybe generate from initial-deployment script and give from command line?
    * Update the sonarqube token in jenkins.

* This repo needs to be tested on MAC and Windows.

* Need to evaluate what more needs to go into the `initial-deploy.sh` script. The purpose of this script is to bootstrap through the different tools API's what cannot be done statically in the containers.

* Add nuget to jenkins config.

* Trigger seed job from initial-deployment.sh script

## Pre-requisites

* Docker
* Docker Compose

A *reasonably* spec'd computer. GitLab is especially heavy to run and takes several minutes to get *healthy* on my laptop which is running Fedora and has 16GB memory and a quad core processor with 8 threads. 

If running Docker Desktop on Windows or Mac you will probably need to **tune the settings** regarding memory for the VM to prevent swapping.

Add the following to `hosts` file on the computerrunning docker. 

```
# Local stuff
127.0.0.1	traefik.local.net
127.0.0.1	sonarqube.local.net
127.0.0.1	jenkins.local.net
127.0.0.1	gitlab.local.net
```

**NOTE:** Gitlab uses Elasticsearch.

Elasticsearch uses a mmapfs directory by default to store its indices. The default operating system limits on mmap counts is likely to be too low, which may result in out of memory exceptions.

On Linux, you can increase the limits by running the following command as `root:`

```
sysctl -w vm.max_map_count=262144
``` 

To set this value permanently, update the vm.max_map_count setting in /etc/sysctl.conf. To verify after rebooting, run sysctl vm.max_map_count.

## Quick Start

From the root of the repository.

* `docker-compose build`
* `./initial-deploy.sh`

Now you need to wait for the stack to come up.
```
Creating network "devsecops-demos_default" with the default driver
Creating volume "devsecops-demos_jenkins_data" with default driver
Creating volume "devsecops-demos_sonar_data" with default driver
Creating volume "devsecops-demos_sonar_logs" with default driver
Creating volume "devsecops-demos_sonar_db_volume" with default driver
Creating volume "devsecops-demos_gitlab_config" with default driver
Creating volume "devsecops-demos_gitlab_logs" with default driver
Creating volume "devsecops-demos_gitlab_data" with default driver
Creating gitlab       ... done
Creating jenkins      ... done
Creating traefik      ... done
Creating pg_sonarqube ... done
Creating sonarqube    ... done
Waiting for gitlab to be healthy. Be patient, may take a few minutes.
```

Once gitlab is healthy the `initial-deploy.sh` script pushes **this** repository to gitlab http://gitlab.local.net/root/devsecops-demo. 

Once **all** application are up you can run the **seed** job manually. This will generate the **pipeline** job(s) which can then be ran for demo purposes.

* http://traefik.local.net : Proxies all the applications. The Dashboard is not secured. 
* http://jenkins.local.net : Jenkins CI server. User is `jenkins` and password is `password`.
* http://gitlab.local.net : VCS repo manager. User is `root` and password is `passsword`.
* http://sonarqube.local.net : Static analysis tool. A Jenkins user is also created with the same credentials as for Jenkins itself, i.e. user is `jenkins` and password is `password`. The default user `admin` is also available with password `admin`.

**More to come...**


