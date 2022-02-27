package net.kikkirej.alexandria.license.npm.db

import javax.persistence.*

@Entity(name = "analysis")
class Analysis(
    @Id var id: Long,
)

@Entity(name = "npm_project")
class NPMProject(
    @Id var id: Long = 0,
    @ManyToOne var analysis: Analysis,
)

@Entity(name = "npm_project_dependency")
class NPMProjectDependency(
    @Id var id: Long = 0,
    @ManyToOne var project: NPMProject,
    @ManyToOne var dependency: NPMDependency,
)

@Entity(name = "npm_dependency")
class NPMDependency(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long = 0,
    var name: String,
    @ManyToOne var license: License?,
)

@Entity(name = "license")
class License(@Id @GeneratedValue var id: Long = 0,
              var name: String,
              var licenseId: String,
              var url: String,
              var deprecated: Boolean,
              var osiApproved: Boolean,
              var spdx: Boolean
)