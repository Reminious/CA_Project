using CA_Project.Models;
using Microsoft.Data.SqlClient;
using Microsoft.Extensions.Primitives;
using static Microsoft.EntityFrameworkCore.DbLoggerCategory.Database;

namespace CA_Project.Data
{
    public class GoodsData
    {
        public static List<Good> GetAllGoods()
        {
            List<Good> goods = new List<Good>();
            using (SqlConnection conn=new SqlConnection(Data.CONNECTION_STRING))
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
        public static List<Good> GetProductByName(string search)//Get Product BY Name
        {
            List<Good> goods = new List<Good>();
            using (SqlConnection conn = new SqlConnection(Data.CONNECTION_STRING))
            {
                conn.Open();
                string sql = "select * from Goods where Goods.Name LIKE" + "'%"+ search + "%'"+ "OR Goods.Intro LIKE"+ "'%" + search + "%'";
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
                };
            }
            return goods;
        }

        public static void AddToCart(int ProductId, int? UserId,ShopContext db,int AddAmount)
        {

            using (SqlConnection conn = new SqlConnection(Data.CONNECTION_STRING))
            {
                conn.Open();
                //In our database, try to find an item that already exists.
                Cart? findcart = db.Carts.FirstOrDefault(x => x.ProductId == ProductId&&x.UserId== UserId);
                //if this user already has the product in cart, amount++
                if (findcart != null)
                {
                    // Use parameterized queries to prevent SQL injection
                    string sql = "UPDATE Cart SET Amount=@Amount WHERE ProductID=@ProductId AND UserID=@UserId";
                    SqlCommand updateCommand = new SqlCommand(sql, conn);

                    int NewAmount = findcart.Amount+AddAmount;
                    updateCommand.Parameters.AddWithValue("@Amount", NewAmount); 
                    updateCommand.Parameters.AddWithValue("@ProductID", ProductId); 
                    updateCommand.Parameters.AddWithValue("@UserID", UserId);
                    int rowsAffected = updateCommand.ExecuteNonQuery();
                }

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
