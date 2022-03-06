package net.kikkirej.alexandria.license.npm.db

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.*

interface NPMDependencyRepository : CrudRepository<NPMDependency, Long>{

    @Query("select (count(nd)>0) from npm_project_dependency npd " +
            "join npd.dependency nd " +
            "join npd.project np " +
            "join np.analysis a " +
            "where a.id=?1 " +
            "and nd.license is not null")
    fun existDependenciesWithoutLicense(analysisId: Long) : Boolean

    fun findByName(name: String): Optional<NPMDependency>
}

interface LicenseRepository: CrudRepository<License, Long> {
    fun findByLicenseId(name: String): Optional<License>;

}