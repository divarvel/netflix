package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.ws.WSClient
import scala.concurrent.ExecutionContext
import scala.concurrent.Future


import models.dto.Movie
/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject() (ws: WSClient, context: ExecutionContext) extends Controller {
  implicit val ec = context
  def getMovie(title: String) = Action.async { implicit request =>
    val response = for {
      movie <- getMovieByName(title)
      otherMovies <- getMoviesByDirector(movie.asOpt.map(_.director).getOrElse(""))
    } yield movie -> otherMovies
    response.map({
      case (JsSuccess(m, _), JsSuccess(ms, _)) => {
        val otherMovies =
          ms
          .filterNot(_.show_title == m.show_title)
          .sortBy(_.release_year)
          .reverse

        Ok(views.html.movieDisplay(m, otherMovies))
      }
      case _ => InternalServerError
    })
  }

  def getMovieByName(title: String): Future[JsResult[Movie]] = {
      ws.url("http://netflixroulette.net/api/api.php")
        .withQueryString("title" -> title)
        .get
        .map(r => Json.fromJson[Movie](r.json))
  }

  def getMoviesByDirector(director: String): Future[JsResult[List[Movie]]] = {
      ws.url("http://netflixroulette.net/api/api.php")
        .withQueryString("director" -> director)
        .get
        .map(r => Json.fromJson[List[Movie]](r.json))
  }

  def test(test: Int) = Action { implicit request =>
    Ok(Json.obj("value" -> test))
  }
  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action { implicit request =>
    Ok(views.html.index("<script>alert('coucou')</script>"))
  }

}
