INSERT INTO PAGACOIN_T_USERS (ID_USER, USERNAME, STATUS, NAME, LASTNAME, AGE, CELLPHONE, EMAIL) VALUES
  (1588, '1987diegog', 'ENABLED', 'Diego', 'Gonzalez', 32, '+59899267337', '1987diegog@gmail.com'),
  (1878, 'luchoSuarez', 'ENABLED', 'Luis', 'Suarez', 32, '+59812345678', 'luisito@gmail.com'),
  (1942, 'elMatador', 'ENABLED', 'Edinson', 'Cavani', 32, '+59897645789', 'elEdi@gmail.com'),
  (1999, 'thegoatRF', 'ENABLED', 'Roger', 'Federer', 38, '+59815948678', 'theGOAT@gmail.com'),
  (2506, 'rafael19', 'ENABLED', 'Rafael', 'Nadal', 34, '+59832894578', 'elRafa@gmail.com'),
  (2789, 'silnarbaiz', 'ENABLED', 'Silva', 'Narbaiz', 37, '+59899545546', 'silnarbaiz@gmail.com');
  
INSERT INTO PAGACOIN_T_WALLETS (ID_WALLET, HASH, NAME, BALANCE, TYPE_COIN, CREATED, ID_USER) VALUES
  (78945, 'hash123', 'Wallet PagaCoin', 2500, 'PAGACOIN', '2019-12-15 12:27:35', 1588),
  (78951, 'hash321', 'Wallet PagaCoin', 2500, 'PAGACOIN', '2019-11-10 16:48:00', 1588),
  (65874, 'hash789', 'Wallet Ethereum', 2500, 'ETHEREUM', '2019-11-10 16:48:00', 1588),
  (18797, 'hash987', 'Wallet BitCoin', 2500, 'BITCOIN', '2019-11-10 16:48:00', 1588),
  (98547, 'hash654', 'Wallet LiteCoin', 2500, 'LITECOIN', '2019-11-10 16:48:00', 1588),
  (87801, 'hash456', 'Wallet PagaCoin', 500, 'PAGACOIN', '2019-06-07 09:15:05', 2789),
  (35741, 'hash258', 'Wallet BitCoin', 3000, 'BITCOIN', '2019-06-07 09:15:05', 2789),
  (45678, 'hash147', 'Wallet PagaCoin', 5000, 'PAGACOIN', '2019-06-07 09:15:05', 1878),
  (24775, 'hash369', 'Wallet PagaCoin', 2500, 'PAGACOIN', '2019-06-07 09:15:05', 1999);
  
INSERT INTO PAGACOIN_T_TRANSFERS (ID_TRANSFER, ADMIN_NAME, AMOUNT, TYPE_COIN, TIMESTAMP, ID_ORIGIN_WALLET, ID_DESTINATION_WALLET) VALUES
  (10568, 'ADMIN_DIEGO', 2500, 'PAGACOIN', '2019-12-15 12:57:00', 78945, 87801),
  (10364, 'ADMIN_DIEGO', 500, 'PAGACOIN', '2019-12-16 15:44:08', 87801, 78945),
  (10268, 'ADMIN_DIEGO', 220, 'BITCOIN', '2019-12-10 22:24:32', 18797, 35741);


