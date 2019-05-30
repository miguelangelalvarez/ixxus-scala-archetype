pipeline {
    agent {
        docker {
            image 'swat/scala-docker-builder'
            args '-u root:root -v /var/run/docker.sock:/var/run/docker.sock:ro'
        }
    }
    environment {
        SBT_OPTS = "-Duser.home=${WORKSPACE}"
        SONAR_SCANNER_HOME = '/opt/sonar-scanner'
    }
    options {
        timeout(time: 30, unit: 'MINUTES')
    }
    stages {
        stage('compile') {
            steps {
                sh 'sbt clean compile'
            }
        }
        stage('analysis') {
            parallel {
                stage('style') {
                    steps {
                        sh 'sbt scalastyle'
                    }
                }
                stage('scapegoat') {
                    steps {
                        sh 'sbt scapegoat'
                    }
                }
            }
        }
        stage('Sonar') {
            steps {
                sh 'sbt sonarScan'
            }
        }
    }
    post {
        always {
            archiveArtifacts allowEmptyArchive: true, artifacts: 'target/scalastyle-result.xml', fingerprint: true
            archiveArtifacts allowEmptyArchive: true, artifacts: 'target/**/scapegoat-report/scapegoat*', fingerprint: true

            archiveArtifacts allowEmptyArchive: true, artifacts: '.scannerwork/report-task.txt', fingerprint: true

            archiveArtifacts allowEmptyArchive: true, artifacts: 'target/**/*.jar', fingerprint: true

            cleanWs()
        }
    }
}
