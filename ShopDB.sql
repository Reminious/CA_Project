USE [ShopDB]
GO
/****** Object:  Table [dbo].[Cart]    Script Date: 2023/10/16 9:36:22 ******/
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
/****** Object:  Table [dbo].[Goods]    Script Date: 2023/10/16 9:36:22 ******/
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
/****** Object:  Table [dbo].[PurchaseRecords]    Script Date: 2023/10/16 9:36:22 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[PurchaseRecords](
	[UserID] [int] NOT NULL,
	[ProductID] [int] NOT NULL,
	[ItemID] [varchar](50) NOT NULL,
	[ActCode] [varchar](50) NOT NULL,
 CONSTRAINT [PK_PurchaseRecords] PRIMARY KEY CLUSTERED 
(
	[UserID] ASC,
	[ProductID] ASC,
	[ItemID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Users]    Script Date: 2023/10/16 9:36:22 ******/
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
INSERT [dbo].[Cart] ([ProductID], [UserID], [Amount]) VALUES (1, 1, 13)
INSERT [dbo].[Cart] ([ProductID], [UserID], [Amount]) VALUES (2, 1, 6)
INSERT [dbo].[Cart] ([ProductID], [UserID], [Amount]) VALUES (3, 1, 1)
INSERT [dbo].[Cart] ([ProductID], [UserID], [Amount]) VALUES (6, 1, 1)
GO
SET IDENTITY_INSERT [dbo].[Goods] ON 

INSERT [dbo].[Goods] ([ProductID], [Name], [Price], [Stock], [Intro], [Image]) VALUES (1, N'.Net Charts', CAST(99.00 AS Decimal(8, 2)), 10, N'Brings powerful charting capabilities to your .Net applications.', N'https://dotnetcharting.com/images/fpImages/v10fp4.png')
INSERT [dbo].[Goods] ([ProductID], [Name], [Price], [Stock], [Intro], [Image]) VALUES (2, N'.Net Paypal', CAST(69.00 AS Decimal(8, 2)), 100, N'Integrate your .NET apps with PayPal the easy way!', N'https://techtolia.com/PayPalSubscriptions/assets/images/PayPalNET.PNG')
INSERT [dbo].[Goods] ([ProductID], [Name], [Price], [Stock], [Intro], [Image]) VALUES (3, N'.Net ML', CAST(299.00 AS Decimal(8, 2)), 100, N'Supercharged .NET machine learning libraries.', N'https://repository-images.githubusercontent.com/426603220/514ab5f7-902b-410c-85cd-dd5f1481df77')
INSERT [dbo].[Goods] ([ProductID], [Name], [Price], [Stock], [Intro], [Image]) VALUES (4, N'.Net Analytics', CAST(299.00 AS Decimal(8, 2)), 100, N'Performs data mining and analytics easily in .NET.', N'https://media.licdn.com/dms/image/C510BAQGB4a_CHWGt0A/company-logo_200_200/0/1519867292150?e=2147483647&v=beta&t=_4r6ocJ9amdCjJ977YYSiYYZTjTD5K2X9muwQkPQ9-0')
INSERT [dbo].[Goods] ([ProductID], [Name], [Price], [Stock], [Intro], [Image]) VALUES (5, N'.NET Logger', CAST(49.00 AS Decimal(8, 2)), 111, N'Logs and aggregates events easily in your .NET apps.', N'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTtP-X_P2V2ebUGNBaPKpiJU0M_-ZC9TBRBow&usqp=CAU')
INSERT [dbo].[Goods] ([ProductID], [Name], [Price], [Stock], [Intro], [Image]) VALUES (6, N'.NET Numerics', CAST(199.00 AS Decimal(8, 2)), 111, N'Powerful numerical methods for your .NET simulations.', N'https://numerics.mathdotnet.com/logo.png')
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
