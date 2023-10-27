using System;
using System.Collections.Generic;

namespace CA_Project.Models;

public partial class Good
{
    public int ProductId { get; set; }

    public string Name { get; set; } = null!;

    public decimal Price { get; set; }

    public int Stock { get; set; }

    public string? Intro { get; set; }

    public string? Image { get; set; }

    public virtual ICollection<PurchaseRecord> PurchaseRecords { get; set; } = new List<PurchaseRecord>();
}
