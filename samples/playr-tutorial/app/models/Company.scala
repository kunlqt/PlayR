package models

case class Company(id: Int, name: String) extends CachedItem

object CompanyContainer extends CachedContainer[Company]{
  val cacheKey = "company"

  val defaultItems = Map(
    1 -> Company(1, "Tasty Brewery"),
    2 -> Company(2, "Funny Theater")
  )

  def add(name: String) = {
    addItem(Company(nextId, name))
  }
}
