Sitemaster
======

Yves Hwang
06.03.2015

Setting up your own dev-machine
-------------------------------
You will need:

* Gradle 2.1
* openjdk-7
* mongodb 2.4.9 (have not tested wtih 2.6.5, but will probably work)
* docker if you so desires
* Vagrant & virtualbox

production environment
----------------------
* Ubuntu trusty
* OpenJDK7

To build
--------
gradle war

Unit test
---------
gradle check

Integration test
----------------
gradle integrationTest

api-core
--------
This is the common api component that is published in the SFR Bintray account.

All RESTful API project and products will utilise this library. Naturally each project is free to pin it to a specific verison of the the api-core library of their desire.

see https://github.com/statoilfuelretail/api-core
