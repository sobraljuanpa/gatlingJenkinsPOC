node {

    properties([pipelineTriggers([pollSCM('* * * * *')])])

    stage('Clone source code') {
        git url: 'https://github.com/sobraljuanpa/gatlingJenkinsPOC.git', branch: 'master', credentialsId: 'gitCredentialsJP'
    }

    stage('Execute tests using maven') {
        sh 'mvn clean gatling:test'
    }

    stage('Report results') {
        gatlingArchive()
    }

    stage('Perform post execution checks') {
        gatlingCheck(metrics: [
            'global.responseTime99 = 30',
        ])
    }

}