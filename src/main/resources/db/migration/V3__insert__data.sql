INSERT IGNORE INTO `users` (`id`, `code`, `email`, `is_moderator`, `name`, `password`, `photo`, `reg_time`)
VALUES (1, NULL, 'ivan_ivanov@mail.ru', b'1', 'Иван Иванов',
        'zJ7NsSpZ04Z43hk', NULL, '2021-01-01 04:23:57.000000'),
       (2, NULL, 'petr_petrov@mail.ru', b'0', 'Петр Петров',
        'h2K0nv882W4d', NULL, '2021-02-01 18:24:51.000000'),
       (3, NULL, 'anna_ivanova@mail.ru', b'0', 'Анна Иванова',
        'TQ5uIeg6Ktt3', NULL, '2021-03-28 18:36:47.000000'),
       (4, NULL, 'inna_petrova@mail.ru', b'0', 'Инна Петрова',
        'e2ICZFNcelQVN', NULL, '2021-03-07 04:04:57.000000'),
       (5, NULL, 'pavel1119@mail.ru', b'1', 'Павел Напреенко',
        'BRQnGZsVtlW7BwT', NULL, '2021-01-01 02:29:13.000000');

INSERT IGNORE INTO `posts` (`id`, `is_active`, `moderation_status`, `text`, `time`, `title`, `view_count`, `user_id`,
                            `moderator_id`)
VALUES (1, b'0', 'DECLINED',
        'А также элементы политического процесса формируют глобальную экономическую сеть и при этом - заблокированы в рамках своих собственных рациональных ограничений.',
        '2021-01-01 04:30:27.000000', 'Преступность никогда не была такой неорганизованной', 5, 1, 1),
       (2, b'1', 'NEW',
        'В частности, высокое качество позиционных исследований требует определения и уточнения позиций, занимаемых участниками в отношении поставленных задач.',
        '2021-02-01 18:34:31.000000', 'Дурное дело нехитрое: выбранный нами инновационный путь так же органично вписывается в наши планы', 4, 2, NULL),
       (3, b'0', 'DECLINED',
        'Стремящиеся вытеснить традиционное производство, нанотехнологии призывают нас к новым свершениям, которые, в свою очередь, должны быть ассоциативно распределены по отраслям.',
        '2021-03-28 18:56:17.000000', 'Политика не может не реагировать на глас грядущего поколения', 6, 3, 5),
       (4, b'0', 'DECLINED',
        'Значимость этих проблем настолько очевидна, что социально-экономическое развитие предопределяет высокую востребованность глубокомысленных рассуждений.',
        '2021-04-07 04:54:27.000000', 'Новая модель организационной деятельности разочаровала', 3, 4, 1),
       (5, b'1', 'ACCEPTED',
        'Современные технологии достигли такого уровня, что граница обучения кадров требует анализа системы обучения кадров, соответствующей насущным потребностям.',
        '2021-05-01 02:39:23.000000', 'Очевидцы сообщают, что слышали шопот бессменных лидеров', 7, 5, 5),
       (6, b'1', 'ACCEPTED',
        'Ясность нашей позиции очевидна: сплочённость команды профессионалов создаёт необходимость включения в производственный план целого ряда внеочередных мероприятий с учётом комплекса переосмысления внешнеэкономических политик.',
        '2021-05-13 05:30:27.000000', 'Инцидент не исчерпан: объемы выросли', 4, 1, 1),
       (7, b'1', 'NEW',
        'В своём стремлении улучшить пользовательский опыт мы упускаем, что некоторые особенности внутренней политики формируют глобальную экономическую сеть и при этом - разоблачены.',
        '2021-06-11 16:32:31.000000', 'Коронованный герцог графства стал нашим флагом в борьбе с ложью', 8, 2, NULL),
       (8, b'1', 'DECLINED',
        'Господа, выбранный нами инновационный путь способствует подготовке и реализации позиций, занимаемых участниками в отношении поставленных задач.',
        '2021-06-18 20:26:17.000000', 'Коронованный герцог графства обнадёживает', 5, 3, 1),
       (9, b'1', 'DECLINED',
        'В частности, семантический разбор внешних противодействий играет определяющее значение для как самодостаточных, так и внешне зависимых концептуальных решений.',
        '2021-07-01 14:34:27.000000', 'Консультация с широким активом разочаровала', 3, 4, 5),
       (10, b'1', 'ACCEPTED',
        'Каждый из нас понимает очевидную вещь: существующая теория в значительной степени обусловливает важность соответствующих условий активизации.',
        '2021-07-07 22:19:23.000000', 'Глубокий уровень погружения станет частью наших традиций', 2, 5, 1),
       (11, b'0', 'DECLINED',
        'А также элементы политического процесса формируют глобальную экономическую сеть и при этом - заблокированы в рамках своих собственных рациональных ограничений.',
        '2021-01-01 04:31:27.000000', 'Преступность никогда не была такой неорганизованной', 5, 1, 1),
       (12, b'1', 'NEW',
        'В частности, высокое качество позиционных исследований требует определения и уточнения позиций, занимаемых участниками в отношении поставленных задач.',
        '2021-02-01 18:35:31.000000', 'Дурное дело нехитрое: выбранный нами инновационный путь так же органично вписывается в наши планы', 4, 2, NULL),
       (13, b'0', 'DECLINED',
        'Стремящиеся вытеснить традиционное производство, нанотехнологии призывают нас к новым свершениям, которые, в свою очередь, должны быть ассоциативно распределены по отраслям.',
        '2021-03-28 18:57:17.000000', 'Политика не может не реагировать на глас грядущего поколения', 6, 3, 5),
       (14, b'0', 'DECLINED',
        'Значимость этих проблем настолько очевидна, что социально-экономическое развитие предопределяет высокую востребованность глубокомысленных рассуждений.',
        '2021-04-07 04:55:27.000000', 'Новая модель организационной деятельности разочаровала', 3, 4, 1),
       (15, b'1', 'ACCEPTED',
        'Современные технологии достигли такого уровня, что граница обучения кадров требует анализа системы обучения кадров, соответствующей насущным потребностям.',
        '2021-05-01 02:49:23.000000', 'Очевидцы сообщают, что слышали шопот бессменных лидеров', 7, 5, 5),
       (16, b'1', 'ACCEPTED',
        'Ясность нашей позиции очевидна: сплочённость команды профессионалов создаёт необходимость включения в производственный план целого ряда внеочередных мероприятий с учётом комплекса переосмысления внешнеэкономических политик.',
        '2021-05-13 05:31:27.000000', 'Инцидент не исчерпан: объемы выросли', 4, 1, 1),
       (17, b'1', 'NEW',
        'В своём стремлении улучшить пользовательский опыт мы упускаем, что некоторые особенности внутренней политики формируют глобальную экономическую сеть и при этом - разоблачены.',
        '2021-06-11 16:33:31.000000', 'Коронованный герцог графства стал нашим флагом в борьбе с ложью', 8, 2, NULL),
       (18, b'1', 'DECLINED',
        'Господа, выбранный нами инновационный путь способствует подготовке и реализации позиций, занимаемых участниками в отношении поставленных задач.',
        '2021-06-18 20:36:17.000000', 'Коронованный герцог графства обнадёживает', 5, 3, 1),
       (19, b'1', 'DECLINED',
        'В частности, семантический разбор внешних противодействий играет определяющее значение для как самодостаточных, так и внешне зависимых концептуальных решений.',
        '2021-07-01 14:35:27.000000', 'Консультация с широким активом разочаровала', 3, 4, 5),
       (20, b'1', 'ACCEPTED',
        'Каждый из нас понимает очевидную вещь: существующая теория в значительной степени обусловливает важность соответствующих условий активизации.',
        '2021-07-07 22:29:23.000000', 'Глубокий уровень погружения станет частью наших традиций', 2, 5, 1);


INSERT IGNORE INTO `post_comments` (`id`, `text`, `time`, `parent_id`, `post_id`, `user_id`)
VALUES (1, 'Звон колоколов', '2021-01-13 04:40:27.000000', NULL, 1, 2),
       (2, 'Боевой клич героев', '2021-02-01 18:54:31.000000', NULL, 2, 3),
       (3, 'Цена вопроса не важна', '2021-03-28 19:16:17.000000', NULL, 3, 4),
       (4, 'Только зима близко', '2021-04-07 06:44:27.000000', NULL, 4, 5),
       (5, 'Консультация ни к чему нас не обязывает', '2021-05-01 02:49:23.000000', NULL, 5, 1),
       (6, 'Старческий скрип Амстердама', '2021-05-13 05:50:27.000000', 1, 1, 3),
       (7, 'Методология разработки продолжает удивлять', '2021-06-11 16:42:31.000000', 2, 2, 4),
       (8, 'Не следует забывать, что небо темнеет', '2021-06-18 20:56:17.000000', 3, 3, 5),
       (9, 'Доблесть наших продолжает удивлять', '2021-07-01 14:44:27.000000', 4, 4, 1),
       (10, 'Поразительное заявление', '2021-07-07 22:39:23.000000', 5, 5, 2),
       (11, 'Никто не может', '2021-07-13 04:50:27.000000', NULL, 6, 2),
       (12, 'Кровь стынет в жилах', '2021-08-01 19:14:31.000000', NULL, 7, 3),
       (13, 'Потускнели светлые лики икон', '2021-08-18 19:26:17.000000', NULL, 8, 4),
       (14, 'Воистину радостный звук', '2021-08-28 07:24:27.000000', NULL, 9, 5),
       (15, 'Консультация разочаровала', '2021-09-01 03:19:23.000000', NULL, 10, 1),
       (16, 'Курс определил дальнейшее развитие', '2021-09-03 06:20:27.000000', 11, 6, 3),
       (17, 'Новая модель решения', '2021-09-04 16:52:31.000000', 12, 7, 4),
       (18, 'Глубокий уровень погружения', '2021-09-05 21:36:17.000000', 13, 8, 5),
       (19, 'Свободу слова не задушить', '2021-09-06 15:24:27.000000', 14, 9, 1),
       (20, 'Расправили крылья', '2021-09-07 13:19:23.000000', 15, 10, 2),
       (21, 'Звон колоколов', '2021-01-13 04:40:27.000000', NULL, 11, 2),
       (22, 'Боевой клич героев', '2021-02-01 18:54:31.000000', NULL, 12, 3),
       (23, 'Цена вопроса не важна', '2021-03-28 19:16:17.000000', NULL, 13, 4),
       (24, 'Только зима близко', '2021-04-07 06:44:27.000000', NULL, 14, 5),
       (25, 'Консультация ни к чему нас не обязывает', '2021-05-01 02:49:23.000000', NULL, 15, 1),
       (26, 'Старческий скрип Амстердама', '2021-05-13 05:50:27.000000', 21, 11, 3),
       (27, 'Методология разработки продолжает удивлять', '2021-06-11 16:42:31.000000', 22, 12, 4),
       (28, 'Не следует забывать, что небо темнеет', '2021-06-18 20:56:17.000000', 23, 13, 5),
       (29, 'Доблесть наших продолжает удивлять', '2021-07-01 14:44:27.000000', 24, 14, 1),
       (30, 'Поразительное заявление', '2021-07-07 22:39:23.000000', 25, 15, 2),
       (31, 'Никто не может', '2021-07-13 04:50:27.000000', NULL, 16, 2),
       (32, 'Кровь стынет в жилах', '2021-08-01 19:14:31.000000', NULL, 17, 3),
       (33, 'Потускнели светлые лики икон', '2021-08-18 19:26:17.000000', NULL, 18, 4),
       (34, 'Воистину радостный звук', '2021-08-28 07:24:27.000000', NULL, 19, 5),
       (35, 'Консультация разочаровала', '2021-09-01 03:19:23.000000', NULL, 20, 1),
       (36, 'Курс определил дальнейшее развитие', '2021-09-03 06:20:27.000000', 31, 16, 3),
       (37, 'Новая модель решения', '2021-09-04 16:52:31.000000', 32, 17, 4),
       (38, 'Глубокий уровень погружения', '2021-09-05 21:36:17.000000', 33, 18, 5),
       (39, 'Свободу слова не задушить', '2021-09-06 15:24:27.000000', 34, 19, 1),
       (40, 'Расправили крылья', '2021-09-07 13:19:23.000000', 35, 20, 2);

INSERT IGNORE INTO `tags` (`id`, `name`)
VALUES (1, 'важно'),
       (2, 'горячее'),
       (3, 'идея'),
       (4, 'москва'),
       (5, 'новость'),
       (6, 'программирование'),
       (7, 'технологии');

INSERT IGNORE INTO `tag2post` (`post_id`, `tag_id`)
VALUES (1, 1),
       (1, 2),
       (2, 4),
       (2, 5),
       (3, 3),
       (3, 5),
       (4, 1),
       (5, 7),
       (6, 2),
       (6, 3),
       (6, 5),
       (7, 1),
       (8, 1),
       (9, 4),
       (9, 5),
       (10, 6),
       (10, 7),
       (11, 1),
       (11, 2),
       (12, 4),
       (12, 5),
       (13, 3),
       (13, 5),
       (14, 1),
       (15, 7),
       (16, 2),
       (16, 3),
       (16, 5),
       (17, 1),
       (18, 1),
       (19, 4),
       (19, 5),
       (20, 6),
       (20, 7);

INSERT IGNORE INTO `post_votes` (`id`, `time`, `value`, `post_id`, `user_id`)
VALUES (1, '2021-09-08 13:39:23.000000', 1, 1, 2),
       (2, '2021-09-08 13:39:23.000000', 1, 1, 3),
       (3, '2021-09-08 13:39:23.000000', -1, 1, 4),
       (4, '2021-09-08 13:39:23.000000', -1, 1, 5),
       (5, '2021-09-08 13:39:23.000000', 1, 2, 1),
       (6, '2021-09-08 13:39:23.000000', -1, 2, 3),
       (8, '2021-09-08 13:39:23.000000', -1, 2, 4),
       (13, '2021-09-08 13:39:23.000000', 1, 2, 5),
       (17, '2021-09-08 13:39:23.000000', 1, 3, 1),
       (18, '2021-09-08 13:39:23.000000', 1, 3, 2),
       (19, '2021-09-08 13:39:23.000000', -1, 3, 4),
       (24, '2021-09-08 13:39:23.000000', -1, 3, 5),
       (26, '2021-09-08 13:39:23.000000', -1, 4, 1),
       (28, '2021-09-08 13:39:23.000000', -1, 4, 2),
       (34, '2021-09-08 13:39:23.000000', 1, 4, 3),
       (35, '2021-09-08 13:39:23.000000', 1, 4, 5),
       (47, '2021-09-08 13:39:23.000000', 1, 5, 1),
       (50, '2021-09-08 13:39:23.000000', 1, 5, 2),
       (51, '2021-09-08 13:39:23.000000', 1, 5, 3),
       (52, '2021-09-08 13:39:23.000000', 1, 5, 4),
       (54, '2021-09-08 13:39:23.000000', -1, 6, 2),
       (56, '2021-09-08 13:39:23.000000', -1, 6, 3),
       (57, '2021-09-08 13:39:23.000000', 1, 6, 4),
       (58, '2021-09-08 13:39:23.000000', -1, 6, 5),
       (59, '2021-09-08 13:39:23.000000', -1, 7, 1),
       (60, '2021-09-08 13:39:23.000000', -1, 7, 3),
       (65, '2021-09-08 13:39:23.000000', -1, 7, 4),
       (71, '2021-09-08 13:39:23.000000', -1, 7, 5),
       (72, '2021-09-08 13:39:23.000000', 1, 8, 1),
       (73, '2021-09-08 13:39:23.000000', 1, 8, 2),
       (75, '2021-09-08 13:39:23.000000', 1, 8, 4),
       (76, '2021-09-08 13:39:23.000000', 1, 8, 5),
       (77, '2021-09-08 13:39:23.000000', -1, 9, 1),
       (78, '2021-09-08 13:39:23.000000', -1, 9, 2),
       (80, '2021-09-08 13:39:23.000000', -1, 9, 3),
       (81, '2021-09-08 13:39:23.000000', -1, 9, 5),
       (82, '2021-09-08 13:39:23.000000', 1, 10, 1),
       (84, '2021-09-08 13:39:23.000000', 1, 10, 2),
       (90, '2021-09-08 13:39:23.000000', 1, 10, 3),
       (92, '2021-09-08 13:39:23.000000', 1, 10, 4),
       (93, '2021-09-08 13:39:23.000000', 1, 11, 2),
       (94, '2021-09-08 13:39:23.000000', 1, 11, 3),
       (95, '2021-09-08 13:39:23.000000', -1, 11, 4),
       (96, '2021-09-08 13:39:23.000000', -1, 11, 5),
       (97, '2021-09-08 13:39:23.000000', 1, 12, 1),
       (98, '2021-09-08 13:39:23.000000', -1, 12, 3),
       (99, '2021-09-08 13:39:23.000000', -1, 12, 4),
       (100, '2021-09-08 13:39:23.000000', 1, 12, 5),
       (101, '2021-09-08 13:39:23.000000', 1, 13, 1),
       (102, '2021-09-08 13:39:23.000000', 1, 13, 2),
       (103, '2021-09-08 13:39:23.000000', -1, 13, 4),
       (104, '2021-09-08 13:39:23.000000', -1, 13, 5),
       (105, '2021-09-08 13:39:23.000000', -1, 14, 1),
       (106, '2021-09-08 13:39:23.000000', -1, 14, 2),
       (107, '2021-09-08 13:39:23.000000', 1, 14, 3),
       (108, '2021-09-08 13:39:23.000000', 1, 14, 5),
       (109, '2021-09-08 13:39:23.000000', 1, 15, 1),
       (110, '2021-09-08 13:39:23.000000', 1, 15, 2),
       (111, '2021-09-08 13:39:23.000000', 1, 15, 3),
       (112, '2021-09-08 13:39:23.000000', 1, 15, 4),
       (113, '2021-09-08 13:39:23.000000', -1, 16, 2),
       (114, '2021-09-08 13:39:23.000000', -1, 16, 3),
       (115, '2021-09-08 13:39:23.000000', 1, 16, 4),
       (116, '2021-09-08 13:39:23.000000', -1, 16, 5),
       (117, '2021-09-08 13:39:23.000000', -1, 17, 1),
       (118, '2021-09-08 13:39:23.000000', -1, 17, 3),
       (119, '2021-09-08 13:39:23.000000', -1, 17, 4),
       (120, '2021-09-08 13:39:23.000000', -1, 17, 5),
       (121, '2021-09-08 13:39:23.000000', 1, 18, 1),
       (122, '2021-09-08 13:39:23.000000', 1, 18, 2),
       (123, '2021-09-08 13:39:23.000000', 1, 18, 4),
       (124, '2021-09-08 13:39:23.000000', 1, 18, 5),
       (125, '2021-09-08 13:39:23.000000', -1, 19, 1),
       (126, '2021-09-08 13:39:23.000000', -1, 19, 2),
       (127, '2021-09-08 13:39:23.000000', -1, 19, 3),
       (128, '2021-09-08 13:39:23.000000', -1, 19, 5),
       (129, '2021-09-08 13:39:23.000000', 1, 20, 1),
       (130, '2021-09-08 13:39:23.000000', 1, 20, 2),
       (131, '2021-09-08 13:39:23.000000', 1, 20, 3),
       (132, '2021-09-08 13:39:23.000000', 1, 20, 4);

