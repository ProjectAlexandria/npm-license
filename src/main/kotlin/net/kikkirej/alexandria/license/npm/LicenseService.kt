package net.kikkirej.alexandria.license.npm

import net.kikkirej.alexandria.license.npm.db.License
import net.kikkirej.alexandria.license.npm.db.LicenseRepository
import net.kikkirej.alexandria.license.npm.db.NPMDependency
import net.kikkirej.alexandria.license.npm.db.NPMDependencyRepository
import net.kikkirej.alexandria.license.npm.npm.NpmJsonData
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LicenseService(
    @Autowired val npmDependencyRepository: NPMDependencyRepository,
    @Autowired val licenseRepository: LicenseRepository) {
    
    private val log = LoggerFactory.getLogger(this.javaClass)

    fun handleLicenses(data: Map<String, NpmJsonData>){
        for(libraryAndVersion in data.keys){
            val npmJsonData = data[libraryAndVersion]
            if(npmJsonData?.licenses == null){
                continue
            }
            val library = libraryAndVersion.substring(0, libraryAndVersion.lastIndexOf("@"))
            setLicenseToDependencyInDb(library, npmJsonData.licenses!!)
        }
    }

    private fun setLicenseToDependencyInDb(libraryName: String, licenses: String) {
        val findByName = npmDependencyRepository.findByName(libraryName)
        if(findByName.isPresent) {
            setLicenseRelationToDbObjectIfNecessary(findByName.get(), licenses)
        } else {
            log.error("The dependency $libraryName couldn't been found in the database")
        }
    }

    private fun setLicenseRelationToDbObjectIfNecessary(dbDependency: NPMDependency, licenses: String) {
        if(dbDependency.license == null) {
            dbDependency.license = findOrCreateLicenseInDb(licenses)
            npmDependencyRepository.save(dbDependency)
        }
    }

    private fun findOrCreateLicenseInDb(licenseId: String): License {
        val findByLicenseId = licenseRepository.findByLicenseId(licenseId);
        if(findByLicenseId.isPresent) {
            return findByLicenseId.get()
        } else {
            val newLicense = License(licenseId = licenseId)
            licenseRepository.save(newLicense)
            return newLicense;
        }
    }

}