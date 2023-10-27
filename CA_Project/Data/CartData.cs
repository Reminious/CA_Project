﻿using Microsoft.AspNetCore.Mvc;
using CA_Project.Models;
using Microsoft.Data.SqlClient;
using System;
using CA_Project.Data;



namespace CA_Project.Data
{
    public class CartData
    {
        public static List<Cart> GetAllCartGoods(int? UserId)
        {
            decimal? TotalPrice = 0;
            List<Cart> carts = new List<Cart>();

            //List<Good> cartgoods = new List<Good>();
            using (SqlConnection conn = new SqlConnection(Data.CONNECTION_STRING))
            {
                conn.Open();

                //change the sql instruct
                string sql = @"select * 
                               from Cart"; 
                SqlCommand cmd = new SqlCommand(sql, conn);
                SqlDataReader reader = cmd.ExecuteReader();
                while (reader.Read())
                {
                    Cart cart = new Cart()
                    {
                        ProductId = (int)reader["ProductId"],
                        UserId= (int)reader["UserId"],
                        Amount = (int)reader["Amount"],
                     
                    };
                 
                    if (cart.UserId == UserId && cart.Amount>0)
                    {   
                        carts.Add(cart);
                    }
                    ////this attribute wiil save the info about the orderdetail for this time
                }
                
                return carts;
            }
        }

        public static void AddMyCart(int ProductId, int? UserId, ShopContext db)
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
                    string sql = "UPDATE Cart SET Amount=@Amount WHERE ProductID=@ProductId AND UserID=@UserId";
                    SqlCommand updateCommand = new SqlCommand(sql, conn);

                    int NewAmount = findcart.Amount + 1;
                    updateCommand.Parameters.AddWithValue("@Amount", NewAmount);
                    updateCommand.Parameters.AddWithValue("@ProductID", ProductId);
                    updateCommand.Parameters.AddWithValue("@UserID", UserId);
                    int rowsAffected = updateCommand.ExecuteNonQuery();
                }

                //if this user didn't have this product in his cart, then show nothing.
                //need to change later
                else
                {
                    
                }
                conn.Close();

            };

        }

        public static void AddorderInfo(int? UserId, int ProductId, int Amount,string ItemId, string ActCode, ShopContext db)
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
                    string sql =string.Format("insert into PurchaseRecords(UserId,ProductId,Amount,ItemId,ActCode) values({0},{1},{2},'{3}','{4}')", UserId, ProductId, Amount, ItemId, ActCode);
                    SqlCommand updateCommand = new SqlCommand(sql, conn);

                    int NewAmount = findcart.Amount + 1;
                    updateCommand.Parameters.AddWithValue("@Amount", NewAmount);
                    updateCommand.Parameters.AddWithValue("@ProductID", ProductId);
                    updateCommand.Parameters.AddWithValue("@UserID", UserId);
                    int rowsAffected = updateCommand.ExecuteNonQuery();
                }
                conn.Close();
            };

        }

        public static void MinusMyCart(int ProductId, int? UserId, ShopContext db)
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
                    string sql = "UPDATE Cart SET Amount=@Amount WHERE ProductID=@ProductId AND UserID=@UserId";
                    SqlCommand updateCommand = new SqlCommand(sql, conn);

                    int NewAmount = findcart.Amount - 1;
                    updateCommand.Parameters.AddWithValue("@Amount", NewAmount);
                    updateCommand.Parameters.AddWithValue("@ProductID", ProductId);
                    updateCommand.Parameters.AddWithValue("@UserID", UserId);
                    int rowsAffected = updateCommand.ExecuteNonQuery();
                }

                //if this user didn't have this product in his cart, then show nothing.
                //need to change later
                else
                {

                }
                conn.Close();

            };

        }
    }
}
