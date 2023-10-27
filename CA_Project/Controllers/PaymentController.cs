using CA_Project.Data;
using CA_Project.Models;
using Microsoft.AspNetCore.Mvc;
using System.Net.Mime;

namespace CA_Project.Controllers
{
    public class PaymentController : Controller
    {
        private readonly ShopContext _context;
        private readonly IHttpContextAccessor _HttpContextAccessor;
        public PaymentController(ShopContext context, IHttpContextAccessor HttpContextAccessor)
        {
            _context = context;
            _HttpContextAccessor = HttpContextAccessor;
        }
        public IActionResult Index()
        {
            return View();
        }
        public IActionResult PayCart()
        {
            var records=_context.Carts.Where(p=> p.UserId== (int)_HttpContextAccessor.HttpContext.Session.GetInt32("UserID")).ToList();
            foreach(var x in records)
            {
                int amount = x.Amount;
                var time = DateTime.Now.ToString();
                for (var i = 0; i < amount; i++)
                {                    
                    PurchaseRecord record = new PurchaseRecord()
                    {
                        UserId = (int)_HttpContextAccessor.HttpContext.Session.GetInt32("UserID"),
                        ProductId = x.ProductId,
                        Time = time
                    };
                    string Act = Guid.NewGuid().ToString();
                    record.ItemId = i;
                    record.ActCode = Act;
                    _context.PurchaseRecords.Add(record);
                }                                           
            }
            _context.SaveChanges();
            CartData.DeleteCart((int)_HttpContextAccessor.HttpContext.Session.GetInt32("UserID"));
            return Redirect("/");
        }
    }
}
