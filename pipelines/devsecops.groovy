#!groovy
pipeline {
    agent any 
    environment {
        ORGANIZATION = "eShopOnWeb"
        PROJECT_NAME = "eShopOnWeb"
        DEPLOY_ID = 'Dummy'
    }
    options {
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }
    stages {
        
        stage('Build eShop'){ 
            steps {
                dir("sample_projects/eShopOnWeb"){
                    withSonarQubeEnv('sonarqube.local.net'){sh "dotnet-sonarscanner begin /k:eShopOnWeb"}
                    sh 'dotnet build eShopOnWeb.sln'
                }
            }
        }
        stage('Unit Tests'){
            steps {
                dir("sample_projects/eShopOnWeb"){
                    sh 'dotnet test tests/UnitTests/UnitTests.csproj'
                }
            }
        }
        stage('Integration Tests'){
            steps {
                dir("sample_projects/eShopOnWeb"){
                    sh 'dotnet test tests/IntegrationTests/IntegrationTests.csproj'
                }
            }
        }
        stage('End Analysis'){
            steps {
                dir("sample_projects/eShopOnWeb"){
                    withSonarQubeEnv('sonarqube.local.net') {sh "dotnet-sonarscanner end"}
                    script {
                        sh "echo 'This is a stupid sleep' && sleep 30" 
                        timeout(time: 10, unit: 'MINUTES') {
                            def qg = waitForQualityGate()
                            if (qg.status != 'OK') {error "The SonarQube Quality Gate has failed with: ${qg.status}!..."}
                        }
                    }
                }
            }
        }
        stage('Evaluate') {
            steps { sh "echo add SQ QG here!" }
        }
        stage('Pack') {
            steps {sh "echo Pack or prepare artifact for deployment"}
        }
        stage ('Deploy') {steps {sh "echo Do a deploy here."}}
        stage('DAST') {steps { sh "echo Run Security Tests"}}
    }
    post {
        success {
            sh "echo Do something on success!"
        }
        unstable {
            sh "echo Do something on success!"
        }
        failure {
            sh "echo Do something on success!"
        }
        always {
            sh "echo Do something on success!"
        }
    }
}
