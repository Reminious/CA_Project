using CA_Project.Data;
using CA_Project.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace CA_Project.Controllers
{
    public class GoodsController : Controller
    {
        private  readonly ShopContext db;
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
        public IActionResult AddToCart(int ProductId,string Name)
        {
            int? userId = _HttpContextAccessor.HttpContext.Session.GetInt32("UserID");
            ViewData["UserId"]=userId;
            if(userId == null)
            {
                return RedirectToAction("Index");
            }
            else
            {
                ViewData["Name"] = Name;
                GoodsData.AddToCart(ProductId, userId, db);
                return View();
            }

        }
        [HttpPost]
        public IActionResult Search(string search)
        {
            int? userId = _HttpContextAccessor.HttpContext.Session.GetInt32("UserID");
            ViewData["UserId"] = userId;
            if (userId == null)
            {
                return RedirectToAction("Index");
            }
            else {
                if (search != null)
                {
                    List<Good> goods = GoodsData.GetProductByName(search);
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
}
