using System;
using System.Collections.Generic;
using Microsoft.EntityFrameworkCore;

namespace CA_Project.Models;

public partial class ShopContext : DbContext
{
    public ShopContext()
    {
    }

    public ShopContext(DbContextOptions<ShopContext> options)
        : base(options)
    {
    }

    public virtual DbSet<Cart> Carts { get; set; }

    public virtual DbSet<Good> Goods { get; set; }

    public virtual DbSet<PurchaseRecord> PurchaseRecords { get; set; }

    public virtual DbSet<User> Users { get; set; }
    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.Entity<Cart>(entity =>
        {
            entity.HasKey(e => new { e.ProductId, e.UserId }).HasName("PK__Cart__65744A27F34D13C5");

            entity.ToTable("Cart");

            entity.Property(e => e.ProductId).HasColumnName("ProductID");
            entity.Property(e => e.UserId).HasColumnName("UserID");
        });

        modelBuilder.Entity<Good>(entity =>
        {
            entity.HasKey(e => e.ProductId).HasName("PK_goods");

            entity.Property(e => e.ProductId).HasColumnName("ProductID");
            entity.Property(e => e.Image).IsUnicode(false);
            entity.Property(e => e.Intro).HasColumnType("text");
            entity.Property(e => e.Name)
                .HasMaxLength(50)
                .IsUnicode(false);
            entity.Property(e => e.Price).HasColumnType("decimal(8, 2)");
        });

        modelBuilder.Entity<PurchaseRecord>(entity =>
        {
            entity.HasKey(e => new { e.UserId, e.ProductId, e.ItemId });

            entity.Property(e => e.UserId).HasColumnName("UserID");
            entity.Property(e => e.ProductId).HasColumnName("ProductID");
            entity.Property(e => e.ItemId).HasColumnName("ItemID");
            entity.Property(e => e.ActCode)
                .HasMaxLength(50)
                .IsUnicode(false);
            entity.Property(e => e.Time)
                .HasMaxLength(50)
                .IsUnicode(false);

            entity.HasOne(d => d.Product).WithMany(p => p.PurchaseRecords)
                .HasForeignKey(d => d.ProductId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_Goods_ProductID");

            entity.HasOne(d => d.User).WithMany(p => p.PurchaseRecords)
                .HasForeignKey(d => d.UserId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_Users_UserID");
        });

        modelBuilder.Entity<User>(entity =>
        {
            entity.HasKey(e => e.UserId).HasName("PK_users");

            entity.Property(e => e.UserId).HasColumnName("UserID");
            entity.Property(e => e.Nickname)
                .HasMaxLength(50)
                .IsUnicode(false);
            entity.Property(e => e.Password)
                .HasMaxLength(50)
                .IsUnicode(false);
            entity.Property(e => e.UserName)
                .HasMaxLength(50)
                .IsUnicode(false);
        });

        OnModelCreatingPartial(modelBuilder);
    }

    partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
}
