using CA_Project.Data;
using CA_Project.Models;
using Microsoft.AspNetCore.Mvc;

namespace CA_Project.Controllers
{
    public class PaymentController : Controller
    {
        private readonly ShopContext db;
        private readonly IHttpContextAccessor _HttpContextAccessor;
        public PaymentController(IHttpContextAccessor HttpContextAccessor, ShopContext db)
        {
            _HttpContextAccessor = HttpContextAccessor;
            this.db = db;
        }
        public IActionResult Index(string productId, string amount)
        {
            ViewBag.productId=productId;
            ViewBag.amount = amount;
            return View();
        }
        public IActionResult OrderInfo(string productIds, string amount)
        {
            string[] productIdsArray = productIds.Split(",").Distinct().ToArray();
            string[] amountArray = amount.Split(",").ToArray();
            int? userId = _HttpContextAccessor.HttpContext.Session.GetInt32("UserID");
            for (int i = 0; i < productIdsArray.Length; i++)
            {
                string code = GenerateRandomCode(16);
                CartData.AddorderInfo(userId, int.Parse(productIdsArray[i]), int.Parse(amountArray[i]), DateTime.Now.ToString(), code, db);
            }
            return Redirect("/");
        }

        public IActionResult Records(string productIds, string amount)
        {
            int? userId = _HttpContextAccessor.HttpContext.Session.GetInt32("UserID");
            int id = (int)userId;
            ViewData["UserId"] = userId;
            List<Cart> carts = CartData.GetAllCartGoods(userId);
            var result = db.PurchaseRecords.Where(cart => cart.UserId == userId)
            .Join(db.Goods,
          records => records.ProductId,
          goods => goods.ProductId,

          (records, goods) => new Total
          {
              ProductId = records.ProductId,
              Price = goods.Price,
              Image = goods.Image,
              UserId = id,
              Amount = records.Amount,
              ItemId = records.ItemId,
              ActCode = records.ActCode
          })
      .ToList();
            decimal totalPriceSum = result.Sum(item => item.TotalPricePerProduct);
            List<Total> total = TotalData.Get(userId, result);

            foreach (var p in result)
            {
                string Name = db.Goods.Where(g => g.ProductId == p.ProductId).FirstOrDefault()?.Name;
                ViewData[p.Image] = Name;
            }
            return View(result);
        }
        public static string GenerateRandomCode(int length)
        {
            const string chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            Random random = new Random();
            return new string(Enumerable.Repeat(chars, length)
                .Select(s => s[random.Next(s.Length)]).ToArray());
        }

    }
}
