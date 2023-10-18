using NuGet.Packaging.Signing;

namespace CA_Project.Models
{
    public class Total
    {
        public int ProductId { get; set; }

        public int UserId { get; set; }

        public int Amount { get; set; }
        public decimal Price { get; set; }
        public decimal TotalPricePerProduct { get; set; }
        public string?Image { get; set; }
    }
}

