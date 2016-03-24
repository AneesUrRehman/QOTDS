package simple.login
apply plugin: 'com.bmuschko.docker-remote-api'
import com.bmuschko.gradle.tasks.image.*

def index () { 
	action: home
	}
task buildMyAppImage(type: DockerBuildImage) {
    inputDir = file('docker/myapp')
    tag = 'test/myapp'
}
task createMyAppContainer(type: DockerCreateContainer) {
    dependsOn buildMyAppImage
    targetImageId { buildMyAppImage.getImageId() }
    portBindings = ['8080:8080']
}

task startMyAppContainer(type: DockerStartContainer) {
    dependsOn createMyAppContainer
    targetContainerId { createMyAppContainer.getContainerId() }
}

task stopMyAppContainer(type: DockerStopContainer) {
    targetContainerId { createMyAppContainer.getContainerId() }
}

task functionalTestMyApp(type: Test) {
    dependsOn startMyAppContainer
    finalizedBy stopMyAppContainer
}

    
