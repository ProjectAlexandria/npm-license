package net.kikkirej.alexandria.license.npm

import net.kikkirej.alexandria.license.npm.config.GeneralProperties
import net.kikkirej.alexandria.license.npm.db.NPMDependencyRepository
import net.kikkirej.alexandria.license.npm.npm.NPMCommandRunner
import net.kikkirej.alexandria.license.npm.npm.NpmJsonReader
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription
import org.camunda.bpm.client.task.ExternalTask
import org.springframework.stereotype.Component
import org.camunda.bpm.client.task.ExternalTaskHandler
import org.camunda.bpm.client.task.ExternalTaskService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import java.io.File


@Component
@ExternalTaskSubscription("npm-license")
class NpmLicenseExecutor(
    @Autowired val generalProperties: GeneralProperties,
    @Autowired val npmDependencyRepository: NPMDependencyRepository,
    @Autowired val npmCommandRunner: NPMCommandRunner,
    @Autowired val npmJsonReader: NpmJsonReader,
    @Autowired val licenseService: LicenseService,
) : ExternalTaskHandler {

    val log: Logger = LoggerFactory.getLogger(javaClass)

    override fun execute(externalTask: ExternalTask?, externalTaskService: ExternalTaskService?) {
        try {
            val npmProjectPath = externalTask!!.getVariable<String>("npm_project_path")
            val businessKey = externalTask.businessKey
//            if(!npmDependencyRepository.existDependenciesWithoutLicense(businessKey.toLong())){
//                log.info("For analysis $businessKey no npm-license is missing. Skipping License-Check")
//                externalTaskService!!.complete(externalTask)
//                return
//            }//TODO wieder einbauen
            val projectPath = projectPathOf(npmProjectPath, businessKey)
            log.info("Analyzing Project ${projectPath.absolutePath}")
            npmCommandRunner.npmInstall(projectPath)
            val outputJson = npmCommandRunner.npmLicenseCheck(projectPath)
            val licenseData = npmJsonReader.parseJson(outputJson)
            licenseService.handleLicenses(licenseData)
            externalTaskService!!.complete(externalTask)
        }catch (e: Exception){
            externalTaskService!!.handleBpmnError(externalTask, "unspecified", e.message)
        }
    }

    private fun projectPathOf(npmProjectPath: String, businessKey: String): File {
        return File(File(generalProperties.sharedfolder).absolutePath + File.separator + businessKey + npmProjectPath)
    }
}