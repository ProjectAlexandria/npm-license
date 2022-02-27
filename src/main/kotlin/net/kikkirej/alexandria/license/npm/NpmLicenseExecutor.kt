package net.kikkirej.alexandria.license.npm

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription
import org.camunda.bpm.client.task.ExternalTask
import org.springframework.stereotype.Component
import org.camunda.bpm.client.task.ExternalTaskHandler
import org.camunda.bpm.client.task.ExternalTaskService


@Component
@ExternalTaskSubscription("npm-license")
class NpmLicenseExecutor : ExternalTaskHandler {
    override fun execute(externalTask: ExternalTask?, externalTaskService: ExternalTaskService?) {
        TODO("Not yet implemented")
    }
}