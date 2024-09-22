pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh 'echo "Building..."'
                // 빌드 명령어 추가
            }
        }
        stage('Upload to S3') {
            steps {
                s3Upload(bucket: 'your-s3-bucket', path: 'build.zip', includePathPattern: '**/*')
            }
        }
        stage('Deploy to CodeDeploy') {
            steps {
                script {
                    def deploymentId = sh(script: 'aws deploy create-deployment --application-name your-app --deployment-group-name your-deployment-group --s3-location bucket=your-s3-bucket,key=build.zip,bundleType=zip', returnStdout: true).trim()
                    echo "Deployment ID: ${deploymentId}"
                }
            }
        }
    }
}
