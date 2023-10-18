using Microsoft.AspNetCore.Mvc;

namespace CA_Project.Controllers
{
    public class PaymentController : Controller
    {
        public IActionResult Index()
        {
            return View();
        }
    }
}
