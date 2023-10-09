using System;
using System.Collections.Generic;

namespace CA_Project.Models;

public partial class User
{
    public int UserId { get; set; }

    public string UserName { get; set; } = null!;

    public string Password { get; set; } = null!;

    public string Nickname { get; set; } = null!;

    public virtual ICollection<PurchaseRecord> PurchaseRecords { get; set; } = new List<PurchaseRecord>();
}
