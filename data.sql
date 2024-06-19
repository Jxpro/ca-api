-- 插入10个用户数据
INSERT INTO `user` (`nickname`, `username`, `email`, `password`, `role`)
VALUES
('admin', 'admin', 'admin@example.com', SHA2('admin', 256), 'admin'),
('Alice', 'alice01', 'alice01@example.com', SHA2('password123', 256), 'user'),
('Bob', 'bob02', 'bob02@example.com', SHA2('password123', 256), 'user'),
('Charlie', 'charlie03', 'charlie03@example.com', SHA2('password123', 256), 'user'),
('David', 'david04', 'david04@example.com', SHA2('password123', 256), 'user'),
('Frank', 'frank06', 'frank06@example.com', SHA2('password123', 256), 'user'),
('Grace', 'grace07', 'grace07@example.com', SHA2('password123', 256), 'user'),
('Heidi', 'heidi08', 'heidi08@example.com', SHA2('password123', 256), 'user'),
('Ivan', 'ivan09', 'ivan09@example.com', SHA2('password123', 256), 'user'),
('Judy', 'judy10', 'judy10@example.com', SHA2('password123', 256), 'user');


-- 插入20个subject数据
INSERT INTO `subject` (`common_name`, `organization`, `organizational_unit`, `country`, `state_or_province_name`, `email`)
VALUES
('John Doe', 'Example Corp', 'IT', 'US', 'California', 'john.doe@example.com'),
('Jane Smith', 'Tech Solutions', 'HR', 'US', 'New York', 'jane.smith@example.com'),
('Emily Johnson', 'Innovate LLC', 'Finance', 'US', 'Texas', 'emily.johnson@example.com'),
('Michael Brown', 'Future Inc', 'R&D', 'US', 'Florida', 'michael.brown@example.com'),
('Jessica Davis', 'Alpha Corp', 'Marketing', 'US', 'Washington', 'jessica.davis@example.com'),
('William Wilson', 'Beta Inc', 'Sales', 'US', 'Oregon', 'william.wilson@example.com'),
('Olivia Martinez', 'Gamma LLC', 'Support', 'US', 'Nevada', 'olivia.martinez@example.com'),
('James Anderson', 'Delta Corp', 'Operations', 'US', 'Arizona', 'james.anderson@example.com'),
('Sophia Thomas', 'Epsilon Inc', 'Engineering', 'US', 'Colorado', 'sophia.thomas@example.com'),
('Benjamin Taylor', 'Zeta LLC', 'Admin', 'US', 'Utah', 'benjamin.taylor@example.com'),
('Charlotte Moore', 'Eta Corp', 'Legal', 'US', 'New Mexico', 'charlotte.moore@example.com'),
('Daniel Jackson', 'Theta Inc', 'Procurement', 'US', 'Idaho', 'daniel.jackson@example.com'),
('Ava White', 'Iota LLC', 'IT', 'US', 'Montana', 'ava.white@example.com'),
('Henry Harris', 'Kappa Corp', 'HR', 'US', 'Wyoming', 'henry.harris@example.com'),
('Mia Martin', 'Lambda Inc', 'Finance', 'US', 'Nebraska', 'mia.martin@example.com'),
('Sebastian Lee', 'Mu LLC', 'R&D', 'US', 'Kansas', 'sebastian.lee@example.com'),
('Amelia Perez', 'Nu Corp', 'Marketing', 'US', 'Oklahoma', 'amelia.perez@example.com'),
('Alexander Clark', 'Xi Inc', 'Sales', 'US', 'Arkansas', 'alexander.clark@example.com'),
('Harper Lewis', 'Omicron LLC', 'Support', 'US', 'Louisiana', 'harper.lewis@example.com'),
('Lucas Walker', 'Pi Corp', 'Operations', 'US', 'Alabama', 'lucas.walker@example.com');

-- 插入20个user_key数据
INSERT INTO `user_key` (`algorithm`, `param1`, `param2`)
VALUES
('RSA-2048', '10001', 'cd618655069f0efb20cfa4d429ae09d12f0fcf3d7520ea101c37a835769a9c79a646e135dbddc8e1bb327343298c81244440d3ba822c85ca1c314501bdf910d04fff40509666c2bc945a9955b613a7c9923101966860eca65842c715d1f71f3f2266e8ea4e237b7a2c8ecec44160324ff113853441219d5971b5549d04984412f4e3574312ee9f46f86e90ff204ac675f8f06001b33ad723c7d7f85b47a3ecde45dd32b79afd49ca4dd0e935f202ca8cdc2b93fde9788f6f0bc7389e7e4a58bcc0450c2ce1914509d19d6c3151e29f6deb5d595df1acc6047fa53518d477b6f24addaf3fde1ffd5edf86a6eb8eae62d0e550196518d46c3b986f3623c752de51'),
('RSA-2048', '10001', '9d5081a4016824bab295993903a75a82a5aefdee5c30c2c9788bea6d00537d7ab9db54b9a061287c534fd96aabe54345a916f4128d4d726bf0ec69d18a9840a9a3113a83129960493dd11aaf2827f9310bded9d18011c72d855e9e00a5a6162edc47e1055e87fd1b8d9618bc2f1ce69b6ab2c03452b5a081ea068111ed3be238946a061cf2655a57839e8b743384f09a0a9ab7e701fe18319fbf2350b80c1517675e1e749d26ad5fcff32753bd988588e79b680e628ae9cad38c60472d7f20a88432271a82c6067eef662a5869767b13091472a7bad9fed124a8ba1536c750a35ed66c0030a7201c914d9270960dd2b53b9564463066889737ce3fcd3f3dda15'),
('RSA-2048', '10001', 'e13be7c7dbe9e6b7b6605869a942a9a5a83eb8812846b7aade5a8db084c7f4d44dd25872bed5c2ea674863e5a1f3d1be2ac3abc894012110a9ab61bf7995e03c1b1008e00025d980543f39dd6dac2a12193bc170d152a1ccd2d5d37441ede07cf7e26a6e688da826dae21c3a17cf23a8fcb1948038e116a772830e6edb41a741b9aa4a7ddf77a1c5e5455eec34a2dba41b25e64e5d6e50488f1d5a4b6245db29b7fd41abbc9edfda732bed9601b6840786c77f214907059ee2210a3a323d4928708130611b2589768e22429a7e1fe40b7444d08c4da7a8873b360cf77b2bf5ded979e52454cf21a11d2fb2d185c1796384b9c721a48e15b1d4be90eb44c36f45'),
('RSA-2048', '10001', 'be32ef0297605ce6e73d173a66c28af1973e8c9b11eb50d37ffdc375149f928c8bceb6042fd62e323d1d615b12963dcb7268e048dc3a8dbbe19630e99357b507fae4ec5433fc2da765c4fced5a732d6d4aa27bd97590bc759610f9c60c2424ffe6c961821e0c63e2b59977b818ada0d2d551d1d50457f5b681f906c9697207872be143ddc63656902ef3a88f237c09b5ac229984d6e6e8ca6c87b511fe0e5d745d237bdfc1d610cc4e2410e14022121cbeb323be330f150aadea57b03182753be4a60c489ea615a95c973bbd898912ebc2732e88c16bcda677b1b5c2b67421c211dd81875f6cd3b10ff1a285c137cbedeedeab4801b877a06c53ed02d9ae4fc7'),
('RSA-2048', '10001', '9c54d7c429704a5c48a934fe157fedeccec4486954ee5d45cd5f77d39cd13121e2216c61a8847b08749ce2a430ff91ac8707ab92d5d013f1644384afe145696a3eb6c35ed38c5d485ca3e571346a8fed1920aac53d3715deb5a684f8a4bbefe13b45127e1ad4781050a9dade4c72a5324366b27eb613e454d84fee6dc6248fe4f574747e67d7b1daa1f9849f59cbe576008ec02b23109746db327287a61718ff1dc825717e939b12530a3f0fa857288e66b2116b4659e56f12f99c13138b30d754479f9c7f5e2c51c35c540aa73a9a512be6390aeb49818b57522d72e291f4d1c2a25c0f852e7972abc608521d9f4c88ab5fe6989c1cfdade9a8110cdc26b9a5'),
('RSA-2048', '10001', 'a1aa65179144597af9b51e0a52fea61642786217a93e0b49d82871ce5dbae98495bdf24d089279b7bc65a0c537d31e9523bc43931dee40ff274fd9c476d68ecb6d18f0b207714495ac5fb705ef5ad87c39b92f80c2780b2f7fa733bbf63ccf3fb56182647fcf7e9c5c3646033f4120eba78507ab747fc5580729ee53ddfa4c27f307943c9fc42ab561d4e22e0b267de7865ddf300656c3265f830f64de78a4486a85480d65895ae2c12682ec61a2e1079afa16e92ae40583599c209c057c6b7e36406d2c3cda4cba14a2e11fe52b554d9b201e90bd7bcae13e1e6c0a10ad1d23b6c37ad3e536c0a3ad2bf79e4b3dcb6d868de571df7e3e5b4d94c6a91a6cbccf'),
('RSA-2048', '10001', 'cfd9887108854e10519437d62b35403da90d211458127951c4daefce77edd4d17dfe68dd8cc8d75dc73c7a814b3289816a4fc5d0386228991881e540e2cfbcbfbb0d410590c08a97495dbdcbe29494a4e4ae6acf9799b4f1419e318711b01c232e4e0e00e2fcd7f755e6dca536ff00b14e1a1e786f6b85ed4211c8ff2b4cf57215dde93e0a55a64bc73fee08008614b2207d288c31b176e2e73d73e821fabe1528ca01b86ea1cf31f55657d19f6b5cd863d13c95e99f37b55960cc5179f87d733730f212fa9ae585750c15ba13d557b20644c463dfab450e5fd5af6f94ea87e5b92b81620098fdef116f8da6c6633aa1e92aa17b92798a9c39d4b6a24b2daa25'),
('RSA-2048', '10001', 'ae060ac652a2b5d5ec05b93f39b9cecc633e2f83e3daf3874a6d53f065c019d58c6204993954027e5e24820376a1056326a51f120f611243f843b67c738b0eb3adc27befe04ad448b8147cf953c21828e8ace2f165336f174b9b864a9e8c6eb1d7c25576a75c6526c1dbf2eba5b448391c654e3ce5a7b7137c82049aff6f8d7a026bd723d77812988de37f688189c0f166653e43cabcc25b2e8b590ef69fc22a8e0fdd3d05eaa8f16c32be86607dc82e152c449628733c72176d6c9bd3eda2a4d9c6ef5799cbaa5d7a8339f271d4621fe10dbdc7d61583a050f56c25b70d6f8f22001430f9516838600ac9bed514ca6eb1d0f1da2b347c682b6428c7ad1e7207'),
('RSA-2048', '10001', 'c5716744c92263c80269c78ac634dc14dca36b6ef565775b846a32d24cc8a07ea11bf2d198f96f2559d893ec79dde12931f9f77eb8178aeedcc08f341404e8c1aec488fec8c3b4f6b0b5029a752516485577a738a0bdb910c121ae27fd802e409a67e48f0576a304e850edda87578b1b866cbb1ba55c4ad8f528837b05212f477703df62724dfb32ac22dbccebb02b911f9c2e805ab59c82d1bbf2a7422408649a26df91dab631278f87a5f1471bf111f2dc254913cb27a5a858b0d763cafa825a1f5a47608cb7811d032f63b66770b96f7b3bb5f7d49ad44aa88b394e4a0dabe4069e197e7787906a5b664b6d567223f6f61c8ff74d183c9cc77978c8074205'),
('RSA-2048', '10001', 'ac43e261804abc32cb9d3e6f30f86b467c13ee65855daba1e53aa0932f9032c47a89d53476752a5fc766fde25aa7f271ed5e58e81f408661cbb53fc2352eba19f18c83648e896334c12f64c4d7bc09938e7f33a5bbba3183f7440593bc5f543d03130c4eda31d9fb6ad38b435be4f8b284600aa1479beee68af159786dfaf484ec63ba8611e4686d527deee1eb78ed6933342b2044a0e791cca038e8fe5532b81457cbdaf86533404ee3b2118d5288680b9b96c68fa819388f000202dcb5b4c71288634aa643e2e55117ab2a5e00b8b284f213be9b92b8b1f9c6681fba4c95fd20d4de454e56bac4ae59740d2a4e99442314101364eeb826534ff1922896780f'),
('EC-prime256v1', '9b7468431109d635bda7a08f03119cf86a6408ba25947c3d4ac01993909f7fb0', '73c7cbdb87dc94fb0491a11c5d4833784898f6c70711e5708ffa5949861e5a30'),
('EC-prime256v1', 'dd5c3f4fec7de9080e1110a80c7f4c9768ed9c765febffc1c91c933011bfedd3', 'f45e6153c17399a6fbbc1c1ffb3a0e8d9a5b8b769361b2b7c9f31f11be9e84ab'),
('EC-prime256v1', '505d0a25313630692329631b56dbe8e56fca925ead5c5a46f0a2eb470ac8c4e3', '94f72e2f7c707ac931ebe7ff535652823d9f3f8c98febd5f7c1a100be7c0be0c'),
('EC-prime256v1', '6e2d470837812c10a12bb4352bad57b685d2354a5153a8dff72bee684fc232a0', '63085ae3db018dde09052e2a67b595cbda1d3d8dd0dada7d8417a5f2daa29448'),
('EC-prime256v1', 'dcd3732f8fdd8c580031213e059a9d68fe2430785cd43dab04628a13ef2d8e4d', 'd9d19c44764fb5e7d1b877166c4eba00881402dd3d0753d85232d5088c7d3a3d'),
('EC-prime256v1', '133674f98238803652ebc3cebe018b5e48cc5ee7aca47ddf181941ce15c76c4c', '6a9fe5606af20fb5b6542750eb859c3f5012e95cabd1f0dd41536c874b9ae037'),
('EC-prime256v1', '8e44b77f1c1d46e4857c2b73020964bfa515f67acb7fd7a06ae5e76a2cb6fdfe', '75cdde14cbe28b46e791db87faa56c08f68d2499a0847399d1a931e76f4381a0'),
('EC-prime256v1', '248a7eb611c7b7a3a5c608f8a39ce58cb1abf10c9fe93323330d14c77bc1d56d', '8bdaf9eee0628eadf4f6a798c3c545740e73f2f6ff0a8138e2f51ca0145dc2bf'),
('EC-prime256v1', 'e7cd2b39ec7845a8c2a88b7cd2cfb433910b77c8636e2ef5141993b8a188669a', 'edf9cf511e79b31e70494b4b5d49eb6fef46ce4d2a999b30d31ada7bf7660cc8'),
('EC-prime256v1', '2bff15a039545c4cd64c573886fc664fd9055c1fb5d0b5e0c05e7ab19d1c188c', '782b7f74af0f89e147e4cafc59a8b0ff3c396f53e71ce0c7c33ba55e6fa6466a');

-- 插入20个request数据
INSERT INTO `request` (`user_id`, `subject_id`, `key_id`, `serial_number`, `not_before`, `not_after`, `revoke_time`, `state`) VALUES
(33, 45, 48, 8513000042575663, '2022-05-22 16:28:56', '2023-05-21 16:28:56', '2023-05-21 16:28:56', '已撤销'),
(39, 39, 45, 9972262255109321, '2023-07-01 18:32:28', '2024-06-30 18:32:28', '2023-07-25 11:42:11', '已撤销'),
(36, 37, 40, 2124334546861701, '2022-12-25 04:24:57', '2023-12-24 04:24:57', '2023-12-24 04:24:57', '已撤销'),
(35, 46, 35, 6662286581849394, '2023-08-29 05:01:18', '2024-08-28 05:01:18', '2024-01-10 21:00:30', '已撤销'),
(33, 33, 36, 3221422109708317, '2023-10-17 07:48:02', '2024-10-16 07:48:02', '2024-03-07 00:19:48', '已撤销'),
(39, 47, 33, 3404725002639780, '2023-09-30 13:26:21', '2024-09-29 13:26:21', '2024-05-09 05:24:05', '已撤销'),
(39, 38, 37, 9335182856808166, '2023-09-06 13:13:48', '2024-09-05 13:13:48', NULL, '已通过'),
(36, 41, 42, 3096793822566950, '2023-12-03 12:12:47', '2024-12-02 12:12:47', NULL, '已通过'),
(35, 50, 44, 7567389541786631, '2024-04-06 01:48:11', '2025-04-05 01:48:11', NULL, '已通过'),
(34, 44, 50, 8402136600241401, '2024-01-13 11:11:45', '2025-01-13 11:11:45', NULL, '已通过'),
(36, 48, 39, 7129230970992268, '2024-06-13 22:48:57', '2025-06-12 22:48:57', NULL, '已通过'),
(32, 36, 43, 1626497020678640, '2024-01-18 19:06:51', '2025-01-18 19:06:51', NULL, '已通过'),
(36, 51, 46, NULL, NULL, NULL, NULL, '待审核'),
(38, 43, 41, NULL, NULL, NULL, NULL, '待审核'),
(40, 34, 47, NULL, NULL, NULL, NULL, '待审核'),
(34, 49, 34, NULL, NULL, NULL, NULL, '待审核'),
(38, 42, 32, NULL, NULL, NULL, NULL, '待审核'),
(32, 40, 51, NULL, NULL, NULL, NULL, '未通过'),
(33, 32, 38, NULL, NULL, NULL, NULL, '未通过'),
(40, 35, 49, NULL, NULL, NULL, NULL, '未通过');