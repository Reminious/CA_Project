using CA_Project.Data;
using CA_Project.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace CA_Project.Controllers
{
    public class GoodsController : Controller
    {
        private readonly ShopContext db;
        private readonly IHttpContextAccessor _HttpContextAccessor;
        public GoodsController(ShopContext db, IHttpContextAccessor HttpContextAccessor)
        {
            this.db = db;
            _HttpContextAccessor = HttpContextAccessor;
        }

        public IActionResult Index()
        {
            List<Good> goods = GoodsData.GetAllGoods();
            ViewData["goods"] = goods;
            return View();

        }

        [HttpPost]
        public IActionResult AddToCart(int ProductId, string Name)
        {
            int? userId = _HttpContextAccessor.HttpContext.Session.GetInt32("UserID");
            ViewData["UserId"] = userId;
            if (userId == null)
            {
                TempData["Message"] = "Please login";
                return RedirectToAction("Index");
            }
            else
            {
                TempData["Message"] = "Successfully added";
                ViewData["Name"] = Name;
                GoodsData.AddToCart(ProductId, userId, db);
                return RedirectToAction("Index");
            }

        }
        [HttpPost]
        public IActionResult Search(string search)
        {
            if (search != null)
            {
                List<Good> goods = GoodsData.GetProductByName(search);
                if (goods.Count == 0)
                {
                    TempData["notice"] = "Not Found";
                }
                ViewData["search"] = goods;
                return View();
            }
            else
            {                
                return RedirectToAction("Index");
            }


        }


    }
}
