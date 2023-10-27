using CA_Project.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace CA_Project.Controllers
{
    public class LoginController : Controller
    {
        private readonly ShopContext _context;
        private readonly IHttpContextAccessor _HttpContextAccessor;
        public LoginController(ShopContext context, IHttpContextAccessor HttpContextAccessor)
        {
            _context = context;
            _HttpContextAccessor = HttpContextAccessor;
        }
        public IActionResult Index()
        {
            ViewBag.Username = _HttpContextAccessor.HttpContext.Request.Cookies["username"];
            ViewBag.Password = _HttpContextAccessor.HttpContext.Request.Cookies["password"];
            //username and password will be default values of respective textbox
            return View();
        }

        [HttpPost]//Use HttpPost to send info from client to server, whitch means here that the username and password we 
        //enterd in our Login page will be send to our  Login Ccontroller.
        public IActionResult Login(string username, string password, int rememberMe)
        {
            User user = _context.Users.Where(p => p.UserName == username && p.Password == password).FirstOrDefault();
            if (user != null)
            {
                if (rememberMe == 1)
                    //when rememberme is ticked
                {
                    CookieOptions option = new CookieOptions();
                    option.Expires = DateTime.Now.AddDays(7);
                    _HttpContextAccessor.HttpContext.Response.Cookies.Append("username", username, option);
                    _HttpContextAccessor.HttpContext.Response.Cookies.Append("password", password, option);
                }
                else
                {
                    _HttpContextAccessor.HttpContext.Response.Cookies.Delete("username");
                    _HttpContextAccessor.HttpContext.Response.Cookies.Delete("password");
                }

                _HttpContextAccessor.HttpContext.Session.SetInt32("UserID",user.UserId);
                _HttpContextAccessor.HttpContext.Session.SetString("Nickname", user.Nickname);

                //use session to remember current user status
                return Redirect("/");
            }
            else
            {          
                return Content("Fail");
            }
        }
        public IActionResult Logout()
        {
            _HttpContextAccessor.HttpContext.Session.Remove("Nickname");
            _HttpContextAccessor.HttpContext.Session.Remove("UserID");
            return Redirect("/");
            //remove contents in current session and return to homepage
        }
    }
}
