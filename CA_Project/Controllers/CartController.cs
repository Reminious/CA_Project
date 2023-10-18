using CA_Project.Data;
using CA_Project.Models;
using Microsoft.AspNetCore.Http.Features;
using Microsoft.AspNetCore.Mvc;
using Microsoft.CodeAnalysis;
using Microsoft.EntityFrameworkCore;
using Microsoft.IdentityModel.Protocols.OpenIdConnect;
using NuGet.Packaging.Signing;
using System.Linq;
namespace CA_Project.Controllers
{


    public class CartController : Controller
    {
        private readonly ShopContext db;
        private readonly IHttpContextAccessor _HttpContextAccessor;
        public CartController(ShopContext db, IHttpContextAccessor HttpContextAccessor)
        {
            this.db = db;
            _HttpContextAccessor = HttpContextAccessor;

        }

        // Other actions




        public IActionResult Index()
        {

            int? userId = _HttpContextAccessor.HttpContext.Session.GetInt32("UserID");
            ViewData["UserId"] = userId;
            List<Cart> carts = CartData.GetAllCartGoods(userId);
            ViewData["carts"] = carts;
            var result = db.Carts.Where(cart => cart.UserId == userId && cart.Amount > 0)
      .Join(db.Goods,
          Carts => Carts.ProductId,
          goods => goods.ProductId,

          (Carts, goods) => new Total
          {
              ProductId = Carts.ProductId,
              Amount = Carts.Amount,
              Price = goods.Price,
              TotalPricePerProduct = goods.Price * Carts.Amount,
              Image = goods.Image
          })
      .ToList();
            decimal totalPriceSum = result.Sum(item => item.TotalPricePerProduct);
            List<Total> total = TotalData.Get(userId, result);
            ViewData["TotalPriceSum"] = totalPriceSum;
            ViewData["result"] = total;

            foreach (var p in result)
            {
                string Name = db.Goods.Where(g=> g.ProductId == p.ProductId).FirstOrDefault()?.Name;
                ViewData[p.Image] = Name;
            }


            return View(result);

        }

        [HttpPost]
        public IActionResult AddMyCart(int ProductId, string Name)
        {
            int? userId = _HttpContextAccessor.HttpContext.Session.GetInt32("UserID");
            ViewData["UserId"] = userId;
            if (userId == null)
            {
                return RedirectToAction("Index");
            }
            else
            {
                ViewData["Name"] = Name;
                CartData.AddMyCart(ProductId, userId, db);
                return RedirectToAction("Index", "Cart");
            }

        }
        [HttpPost]

        public IActionResult MinusMyCart(int ProductId, string Name)
        {
            int? userId = _HttpContextAccessor.HttpContext.Session.GetInt32("UserID");
            ViewData["UserId"] = userId;
            if (userId == null)
            {
                return RedirectToAction("Index");
            }
            else
            {
                ViewData["Name"] = Name;
                CartData.MinusMyCart(ProductId, userId, db);
                return RedirectToAction("Index", "Cart");
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
            else
            {
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
