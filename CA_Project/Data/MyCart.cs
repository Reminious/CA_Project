using CA_Project.Models;
using Microsoft.Data.SqlClient;

namespace CA_Project.Data
{
    public class MyCart
    {
        public static List<Good> GetAllGoods()
        {
            List<Good> goods = new List<Good>();
            using (SqlConnection conn = new SqlConnection(Data.CONNECTION_STRING))
            {
                conn.Open();
                string sql = @"select * 
                               from Goods";
                SqlCommand cmd = new SqlCommand(sql, conn);
                SqlDataReader reader = cmd.ExecuteReader();
                while (reader.Read())
                {
                    Good good = new Good()
                    {
                        ProductId = (int)reader["ProductId"],
                        Name = (string)reader["Name"],
                        Price = (decimal)reader["Price"],
                        Stock = (int)reader["Stock"],
                        Intro = (string)reader["Intro"],
                        Image = (string)reader["Image"],
                    };
                    goods.Add(good);
                }
                return goods;
            }
        }


        //The listed is for search the goods in Cart, for the exact user
        public static void ViewMyCart(int ProductId, int? UserId, ShopContext db)
        {

            using (SqlConnection conn = new SqlConnection(Data.CONNECTION_STRING))
            {
                conn.Open();
                //In our database, try to find an item that already exists.
                Cart? findcart = db.Carts.FirstOrDefault(x => x.ProductId == ProductId && x.UserId == UserId);
                //if this user already has the product in cart, amount++
                if (findcart != null)
                {
                    // Use parameterized queries to prevent SQL injection
                    string sql = "SELECT UserID,ProductId,Amount FROM Cart WHERE UserID=@UserId";
                    SqlCommand ViewCommand = new SqlCommand(sql, conn);

                    //int NewAmount = findcart.Amount + 1;
                    ////THe NewAmount Here need to be changed.


                    //ViewCommand.Parameters.AddWithValue("@Amount", NewAmount);
                    //ViewCommand.Parameters.AddWithValue("@ProductID", ProductId);
                    //ViewCommand.Parameters.AddWithValue("@UserID", UserId);
                    int rowsAffected = ViewCommand.ExecuteNonQuery();
                }

                ////of course you cannot make it to be empty in this part of cart
                //if this user didn't have this product in his cart, then "insert" this productid,userid into table cart.
                else
                {
                    string sql = "INSERT INTO Cart (ProductID, UserID,Amount) VALUES (@ProductId, @UserId,@Amount)";
                    SqlCommand insertCommand = new SqlCommand(sql, conn);

                    // Use parameterized queries to prevent SQL injection
                    insertCommand.Parameters.AddWithValue("@ProductId", ProductId); //Replace the  "@ProductId" in sql with value
                    insertCommand.Parameters.AddWithValue("@UserId", UserId); // same as above
                    insertCommand.Parameters.AddWithValue("@Amount", 1);//same as above
                    int rowsAffected = insertCommand.ExecuteNonQuery();//"ExecuteNonQuery()" is a coommon method to operate database,
                                                                       //usually is used for "insert","update","delete".

                }
                conn.Close();

            };
        }
    }
}
