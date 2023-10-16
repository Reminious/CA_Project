REMENBER TO CHANGE YOUR SETTINGS OF DATABASE：
In appsetting.json, change data source to your servername shown in SMSS. (ex. "Data Source=myServerName" to "Datasource=yourServerName")
In \Data\Data.cs, change CONNECTION_STRING , the server should be your servername. (Same as above, "Server=yourServerName)
In Models\ShopContext.cs, comment out line 26-28 (may vary), your code should be looking like this:
      /* protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
#warning To protect potentially sensitive information in your connection string, you should move it out of source code. You can avoid scaffolding the connection string by using the Name= syntax to read it from configuration - see https://go.microsoft.com/fwlink/?linkid=2131148. For more guidance on storing connection strings, see http://go.microsoft.com/fwlink/?LinkId=723263.
        => optionsBuilder.UseSqlServer("Data Source=.\\SQLEXPRESS;Initial Catalog=ShopDB;Integrated Security=True;Encrypt=True;TrustServerCertificate=True;"); */
