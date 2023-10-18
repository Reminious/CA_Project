using CA_Project.Models;
using Microsoft.AspNetCore.Http.Features;

namespace CA_Project.Data
{
    public class TotalData
    {
        public static List<Total> Get(int? UserId, List<Total> totalinit)
        {
            List<Total> total1 = new List<Total>();
            foreach (var items in totalinit)
            {
                if (items.UserId == UserId)
                {
                    total1.Add(items);
                }

            }
            return total1;

        }

    }
}