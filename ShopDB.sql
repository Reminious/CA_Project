USE [ShopDB]
GO
/****** Object:  Table [dbo].[Cart]    Script Date: 2023/10/27 16:58:09 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Cart](
	[ProductID] [int] NOT NULL,
	[UserID] [int] NOT NULL,
	[Amount] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[ProductID] ASC,
	[UserID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Goods]    Script Date: 2023/10/27 16:58:09 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Goods](
	[ProductID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](50) NOT NULL,
	[Price] [decimal](8, 2) NOT NULL,
	[Stock] [int] NOT NULL,
	[Intro] [text] NULL,
	[Image] [varchar](max) NULL,
 CONSTRAINT [PK_goods] PRIMARY KEY CLUSTERED 
(
	[ProductID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[PurchaseRecords]    Script Date: 2023/10/27 16:58:09 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[PurchaseRecords](
	[UserID] [int] NOT NULL,
	[ProductID] [int] NOT NULL,
	[ItemID] [int] NOT NULL,
	[ActCode] [varchar](50) NOT NULL,
	[Time] [varchar](50) NOT NULL,
 CONSTRAINT [PK_PurchaseRecords] PRIMARY KEY CLUSTERED 
(
	[UserID] ASC,
	[ProductID] ASC,
	[ItemID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Users]    Script Date: 2023/10/27 16:58:09 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Users](
	[UserID] [int] IDENTITY(1,1) NOT NULL,
	[UserName] [varchar](50) NOT NULL,
	[Password] [varchar](50) NOT NULL,
	[Nickname] [varchar](50) NOT NULL,
 CONSTRAINT [PK_users] PRIMARY KEY CLUSTERED 
(
	[UserID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
SET IDENTITY_INSERT [dbo].[Goods] ON 

INSERT [dbo].[Goods] ([ProductID], [Name], [Price], [Stock], [Intro], [Image]) VALUES (1, N'.Net Charts', CAST(99.00 AS Decimal(8, 2)), 10, N'Brings powerful charting capabilities to your .Net applications.', N'https://picdl.sunbangyan.cn/2023/10/27/d9deb7ccadec71a2e1b4fdd607105705.png')
INSERT [dbo].[Goods] ([ProductID], [Name], [Price], [Stock], [Intro], [Image]) VALUES (2, N'.Net Paypal', CAST(69.00 AS Decimal(8, 2)), 100, N'Integrate your .NET apps with PayPal the easy way!', N'https://picst.sunbangyan.cn/2023/10/27/26506ec9f542c704c00d2d68829a79f7.png')
INSERT [dbo].[Goods] ([ProductID], [Name], [Price], [Stock], [Intro], [Image]) VALUES (3, N'.Net ML', CAST(299.00 AS Decimal(8, 2)), 100, N'Supercharged .NET machine learning libraries.', N'https://picss.sunbangyan.cn/2023/10/27/5c499fef5e4c329fd2da20ad99d9ef3c.png')
INSERT [dbo].[Goods] ([ProductID], [Name], [Price], [Stock], [Intro], [Image]) VALUES (4, N'.Net Analytics', CAST(299.00 AS Decimal(8, 2)), 100, N'Performs data mining and analytics easily in .NET.', N'https://picdm.sunbangyan.cn/2023/10/27/bdd6c748dad9ece571e3b302fd5c0dd7.jpg')
INSERT [dbo].[Goods] ([ProductID], [Name], [Price], [Stock], [Intro], [Image]) VALUES (5, N'.NET Logger', CAST(49.00 AS Decimal(8, 2)), 111, N'Logs and aggregates events easily in your .NET apps.', N'https://picss.sunbangyan.cn/2023/10/27/bd945498feff40b4f3d1d8a7c78229de.png')
INSERT [dbo].[Goods] ([ProductID], [Name], [Price], [Stock], [Intro], [Image]) VALUES (6, N'.NET Numerics', CAST(199.00 AS Decimal(8, 2)), 111, N'Powerful numerical methods for your .NET simulations.', N'https://picdl.sunbangyan.cn/2023/10/27/096b5dcec8b7ebaf133afdb926f40d9c.png')
SET IDENTITY_INSERT [dbo].[Goods] OFF
GO
SET IDENTITY_INSERT [dbo].[Users] ON 

INSERT [dbo].[Users] ([UserID], [UserName], [Password], [Nickname]) VALUES (1, N'JohnWick', N'123123', N'J.W')
INSERT [dbo].[Users] ([UserID], [UserName], [Password], [Nickname]) VALUES (2, N'Matty', N'22222', N'Matt')
INSERT [dbo].[Users] ([UserID], [UserName], [Password], [Nickname]) VALUES (3, N'What', N'23333', N'WH')
INSERT [dbo].[Users] ([UserID], [UserName], [Password], [Nickname]) VALUES (4, N'wefwr', N'weqweqe', N'ccc')
SET IDENTITY_INSERT [dbo].[Users] OFF
GO
ALTER TABLE [dbo].[PurchaseRecords]  WITH CHECK ADD  CONSTRAINT [FK_Goods_ProductID] FOREIGN KEY([ProductID])
REFERENCES [dbo].[Goods] ([ProductID])
GO
ALTER TABLE [dbo].[PurchaseRecords] CHECK CONSTRAINT [FK_Goods_ProductID]
GO
ALTER TABLE [dbo].[PurchaseRecords]  WITH CHECK ADD  CONSTRAINT [FK_Users_UserID] FOREIGN KEY([UserID])
REFERENCES [dbo].[Users] ([UserID])
GO
ALTER TABLE [dbo].[PurchaseRecords] CHECK CONSTRAINT [FK_Users_UserID]
GO
