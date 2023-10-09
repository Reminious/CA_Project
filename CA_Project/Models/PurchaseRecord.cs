using System;
using System.Collections.Generic;

namespace CA_Project.Models;

public partial class PurchaseRecord
{
    public int UserId { get; set; }

    public int ProductId { get; set; }

    public string ItemId { get; set; } = null!;

    public string ActCode { get; set; } = null!;

    public virtual Good Product { get; set; } = null!;

    public virtual User User { get; set; } = null!;
}
