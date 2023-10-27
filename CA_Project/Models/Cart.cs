using System;
using System.Collections.Generic;

namespace CA_Project.Models;

public partial class Cart
{
    public int ProductId { get; set; }

    public int UserId { get; set; }

    public int Amount { get; set; }
}
