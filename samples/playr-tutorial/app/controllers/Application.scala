package controllers

import play.api._
import play.api.mvc._
import twentysix.playr.RestApiRouter
import twentysix.playr.RestResourceRouter
import twentysix.playr.GET

object Application extends Controller {

  val crmApi = RestApiRouter()
    .add(PersonController)
    .add(new RestResourceRouter(CompanyController)
      .add("functions", GET, CompanyController.functions _)
      .add("employee", company => EmployeeController(company))
    )

  val api = RestApiRouter()
    .add(ColorController)
    .add("crm" -> crmApi)
}
