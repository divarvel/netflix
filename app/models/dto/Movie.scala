package models.dto

import play.api.libs.json._

case class Movie(
  unit: Int,
  show_id: Int,
  show_title: String,
  release_year: String,
  rating: String,
  category: String,
  show_cast: String,
  director: String,
  summary: String,
  poster: String,
  mediatype: Int,
  runtime: String
)

object Movie {
  implicit val movieFormat = Json.format[Movie]
}
