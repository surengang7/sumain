-- ==========================================
-- Personal Trading DB (PostgreSQL) - DDL
-- No ENUM / No IF NOT EXISTS / With COMMENT
-- ==========================================

-- 1) Master Data
CREATE TABLE exchange (
    exchange_id  bigint PRIMARY KEY,
    exchange_code         text NOT NULL UNIQUE,
    exchange_name         text NOT NULL,
    exchange_timezone     TEXT NOT NULL DEFAULT 'Asia/Shanghai',
    created_time   timestamp time NOT NULL DEFAULT now()
);


COMMENT ON TABLE exchange IS '交易所/市场定义';
COMMENT ON COLUMN exchange.exchange_id IS '主键';
COMMENT ON COLUMN exchange.exchange_code IS '交易所代码：SSE/SZSE/HKEX/NYSE 等';
COMMENT ON COLUMN exchange.exchange_name IS '交易所名称';
COMMENT ON COLUMN exchange.exchange_timezone IS '交易所时区';
COMMENT ON COLUMN exchange.created_time IS '创建时间';

CREATE TABLE security (
    security_id    bigint PRIMARY KEY,
    security_code  TEXT NOT NULL,
    market         TEXT NOT NULL,
    exchange_id    BIGINT NOT NULL REFERENCES exchange(exchange_id),
    security_name  TEXT NOT NULL,
    security_name_en    TEXT,
    security_type       TEXT NOT NULL,               -- STOCK/ETF/INDEX...
    currency       TEXT NOT NULL DEFAULT 'CNY',
    trade_min_size       INT  NOT NULL DEFAULT 100,
    listing_date      DATE,
    delisting_date    DATE,
    security_status         TEXT NOT NULL DEFAULT 'LISTED', -- LISTED/SUSPENDED/DELISTED...
    margin_yn  BOOLEAN,
    created_time     timestamp NOT NULL DEFAULT now(),
    updated_time     timestamp NOT NULL DEFAULT now(),
    UNIQUE (market, security_code)
);

COMMENT ON TABLE security IS '证券主表（股票/ETF/指数等统一抽象）';
COMMENT ON COLUMN security.security_id IS '主键（内部ID）';
COMMENT ON COLUMN security.security_code IS '证券代码：A股如600519/000001，港股如00700';
COMMENT ON COLUMN security.market IS '市场：CN_A/HK/US 等';
COMMENT ON COLUMN security.exchange_id IS '所属交易所ID';
COMMENT ON COLUMN security.security_name IS '证券中文名';
COMMENT ON COLUMN security.security_name_en IS '证券英文名';
COMMENT ON COLUMN security.security_type IS '证券类型：STOCK/ETF/INDEX 等（由代码枚举控制）';
COMMENT ON COLUMN security.currency IS '交易币种';
COMMENT ON COLUMN security.trade_min_size IS '最小交易单位（A股通常100股）';
COMMENT ON COLUMN security.listing_date IS '上市日期';
COMMENT ON COLUMN security.delisting_date IS '退市日期';
COMMENT ON COLUMN security.security_status IS '状态：LISTED/SUSPENDED/DELISTED 等（由代码枚举控制）';
COMMENT ON COLUMN security.margin_yn IS '是否支持融资融券（可空）';
COMMENT ON COLUMN security.created_time IS '创建时间';
COMMENT ON COLUMN security.updated_time IS '更新时间';

CREATE INDEX idx_security_security_code ON security(security_code);
CREATE INDEX idx_security_exchange_id ON security(exchange_id);

CREATE TABLE security_tag (
                              tag_id     BIGSERIAL PRIMARY KEY,
                              taxonomy   TEXT NOT NULL,       -- SW/THS/EM/CUSTOM...
                              tag_type   TEXT NOT NULL,       -- INDUSTRY/CONCEPT/THEME...
                              tag_code   TEXT,
                              tag_name   TEXT NOT NULL,
                              created_time TIMESTAMPTZ NOT NULL DEFAULT now()
);

COMMENT ON TABLE security_tag IS '行业/概念/主题标签维表（口径由 taxonomy 区分）';
COMMENT ON COLUMN security_tag.tag_id IS '主键';
COMMENT ON COLUMN security_tag.taxonomy IS '标签口径：SW/THS/EM/CUSTOM 等';
COMMENT ON COLUMN security_tag.tag_type IS '标签类型：INDUSTRY/CONCEPT/THEME 等';
COMMENT ON COLUMN security_tag.tag_code IS '标签代码（可空）';
COMMENT ON COLUMN security_tag.tag_name IS '标签名称';
COMMENT ON COLUMN security_tag.created_time IS '创建时间';

CREATE UNIQUE INDEX uq_security_tag ON security_tag(taxonomy, tag_type, COALESCE(tag_code, ''), tag_name);

CREATE TABLE security_tag_map (
                                  security_id    BIGINT NOT NULL REFERENCES security(security_id) ON DELETE CASCADE,
                                  tag_id         BIGINT NOT NULL REFERENCES security_tag(tag_id) ON DELETE CASCADE,
                                  weight         NUMERIC(10,4),
                                  effective_from DATE,
                                  effective_to   DATE,
                                  created_time     TIMESTAMPTZ NOT NULL DEFAULT now(),
                                  PRIMARY KEY (security_id, tag_id)
);

COMMENT ON TABLE security_tag_map IS '证券-标签多对多映射';
COMMENT ON COLUMN security_tag_map.security_id IS '证券ID';
COMMENT ON COLUMN security_tag_map.tag_id IS '标签ID';
COMMENT ON COLUMN security_tag_map.weight IS '映射权重/热度（可空）';
COMMENT ON COLUMN security_tag_map.effective_from IS '生效开始日期（可空）';
COMMENT ON COLUMN security_tag_map.effective_to IS '生效结束日期（可空）';
COMMENT ON COLUMN security_tag_map.created_time IS '创建时间';

CREATE INDEX idx_security_tag_map_tag_id ON security_tag_map(tag_id);

CREATE TABLE trading_calendar (
                                  market         TEXT NOT NULL,
                                  trade_date     DATE NOT NULL,
                                  is_trading_day BOOLEAN NOT NULL,
                                  open_time      TIME,
                                  close_time     TIME,
                                  created_time     TIMESTAMPTZ NOT NULL DEFAULT now(),
                                  PRIMARY KEY (market, trade_date)
);

COMMENT ON TABLE trading_calendar IS '交易日历（用于回测、对齐数据、节假日判断）';
COMMENT ON COLUMN trading_calendar.market IS '市场：CN_A/HK/US 等';
COMMENT ON COLUMN trading_calendar.trade_date IS '自然日';
COMMENT ON COLUMN trading_calendar.is_trading_day IS '是否交易日';
COMMENT ON COLUMN trading_calendar.open_time IS '开盘时间（可空）';
COMMENT ON COLUMN trading_calendar.close_time IS '收盘时间（可空）';
COMMENT ON COLUMN trading_calendar.created_time IS '创建时间';

CREATE INDEX idx_trading_calendar_trade_date ON trading_calendar(trade_date);

-- 2) Market Facts (Daily)
CREATE TABLE market_facts_daily (
                                    security_id     BIGINT NOT NULL REFERENCES security(security_id) ON DELETE CASCADE,
                                    trade_date      DATE NOT NULL,
                                    open            NUMERIC(18,6) NOT NULL,
                                    high            NUMERIC(18,6) NOT NULL,
                                    low             NUMERIC(18,6) NOT NULL,
                                    close           NUMERIC(18,6) NOT NULL,
                                    volume          BIGINT NOT NULL,        -- 成交量（股/份）
                                    amount          NUMERIC(20,2),          -- 成交额
                                    turnover_rate   NUMERIC(12,6),          -- 换手率（可空）
                                    pct_chg         NUMERIC(12,6),          -- 涨跌幅（可空）
                                    average_price   NUMERIC(18,6),          -- 成交均价（可空）
                                    suspension_flag BOOLEAN,                -- 停牌标记（可空）
                                    source          TEXT NOT NULL,          -- 数据来源标识
                                    ingested_time   TIMESTAMPTZ NOT NULL DEFAULT now(),
                                    PRIMARY KEY (security_id, trade_date)
);

COMMENT ON TABLE market_facts_daily IS '日线行情（OHLCV）';
COMMENT ON COLUMN market_facts_daily.security_id IS '证券ID';
COMMENT ON COLUMN market_facts_daily.trade_date IS '交易日';
COMMENT ON COLUMN market_facts_daily.open IS '开盘价';
COMMENT ON COLUMN market_facts_daily.high IS '最高价';
COMMENT ON COLUMN market_facts_daily.low IS '最低价';
COMMENT ON COLUMN market_facts_daily.close IS '收盘价';
COMMENT ON COLUMN market_facts_daily.volume IS '成交量（股/份）';
COMMENT ON COLUMN market_facts_daily.amount IS '成交额';
COMMENT ON COLUMN market_facts_daily.turnover_rate IS '换手率';
COMMENT ON COLUMN market_facts_daily.pct_chg IS '涨跌幅';
COMMENT ON COLUMN market_facts_daily.average_price IS '成交均价';
COMMENT ON COLUMN market_facts_daily.suspension_flag IS '是否停牌';
COMMENT ON COLUMN market_facts_daily.source IS '数据来源';
COMMENT ON COLUMN market_facts_daily.ingested_time IS '入库时间';

CREATE INDEX idx_market_facts_daily_trade_date ON market_facts_daily(trade_date);

CREATE TABLE adj_factor (
                            security_id BIGINT NOT NULL REFERENCES security(security_id) ON DELETE CASCADE,
                            trade_date  DATE NOT NULL,
                            factor      NUMERIC(20,10) NOT NULL,  -- 复权因子
                            source      TEXT NOT NULL,
                            ingested_time TIMESTAMPTZ NOT NULL DEFAULT now(),
                            PRIMARY KEY (security_id, trade_date)
);

COMMENT ON TABLE adj_factor IS '复权因子（用于前复权/后复权计算，不覆盖原始价格）';
COMMENT ON COLUMN adj_factor.security_id IS '证券ID';
COMMENT ON COLUMN adj_factor.trade_date IS '交易日';
COMMENT ON COLUMN adj_factor.factor IS '复权因子';
COMMENT ON COLUMN adj_factor.source IS '数据来源';
COMMENT ON COLUMN adj_factor.ingested_time IS '入库时间';

CREATE INDEX idx_adj_factor_trade_date ON adj_factor(trade_date);

CREATE TABLE corporate_action (
                                  action_id      BIGSERIAL PRIMARY KEY,
                                  security_id    BIGINT NOT NULL REFERENCES security(security_id) ON DELETE CASCADE,
                                  action_date    DATE NOT NULL,
                                  action_type    TEXT NOT NULL,          -- DIVIDEND/SPLIT/BONUS/MERGE...
                                  cash_per_share NUMERIC(18,6),
                                  ratio          NUMERIC(18,6),
                                  detail         JSONB,
                                  source         TEXT NOT NULL,
                                  ingested_time    TIMESTAMPTZ NOT NULL DEFAULT now()
);

COMMENT ON TABLE corporate_action IS '公司行为（分红送转拆并股等）';
COMMENT ON COLUMN corporate_action.action_id IS '主键';
COMMENT ON COLUMN corporate_action.security_id IS '证券ID';
COMMENT ON COLUMN corporate_action.action_date IS '发生日期';
COMMENT ON COLUMN corporate_action.action_type IS '公司行为类型（由代码枚举控制）';
COMMENT ON COLUMN corporate_action.cash_per_share IS '每股现金分红（可空）';
COMMENT ON COLUMN corporate_action.ratio IS '比例：拆分/送股/合并等（可空）';
COMMENT ON COLUMN corporate_action.detail IS '扩展信息（JSON）';
COMMENT ON COLUMN corporate_action.source IS '数据来源';
COMMENT ON COLUMN corporate_action.ingested_time IS '入库时间';

CREATE INDEX idx_corporate_action_security_date ON corporate_action(security_id, action_date);

-- 2) Market Facts (1m partitioned by month)
CREATE TABLE market_facts_minute (
                                     security_id    BIGINT NOT NULL REFERENCES security(security_id) ON DELETE CASCADE,
                                     ts             TIMESTAMPTZ NOT NULL,      -- 建议统一存UTC
                                     open           NUMERIC(18,6) NOT NULL,
                                     high           NUMERIC(18,6) NOT NULL,
                                     low            NUMERIC(18,6) NOT NULL,
                                     close          NUMERIC(18,6) NOT NULL,
                                     volume         BIGINT NOT NULL,
                                     amount         NUMERIC(20,2),
                                     average_price  NUMERIC(18,6),
                                     source         TEXT NOT NULL,
                                     ingested_time  TIMESTAMPTZ NOT NULL DEFAULT now(),
                                     PRIMARY KEY (security_id, ts)
) PARTITION BY RANGE (ts);

COMMENT ON TABLE market_facts_minute IS '分钟线行情（1分钟粒度，按 ts 月分区）';
COMMENT ON COLUMN market_facts_minute.security_id IS '证券ID';
COMMENT ON COLUMN market_facts_minute.ts IS '时间戳（分钟）';
COMMENT ON COLUMN market_facts_minute.open IS '开盘价';
COMMENT ON COLUMN market_facts_minute.high IS '最高价';
COMMENT ON COLUMN market_facts_minute.low IS '最低价';
COMMENT ON COLUMN market_facts_minute.close IS '收盘价';
COMMENT ON COLUMN market_facts_minute.volume IS '成交量';
COMMENT ON COLUMN market_facts_minute.amount IS '成交额';
COMMENT ON COLUMN market_facts_minute.average_price IS '成交均价';
COMMENT ON COLUMN market_facts_minute.source IS '数据来源';
COMMENT ON COLUMN market_facts_minute.ingested_time IS '入库时间';

-- 示例分区：2026-01（你按月创建即可）
CREATE TABLE market_facts_minute_202601
    PARTITION OF market_facts_minute
    FOR VALUES FROM ('2026-01-01 00:00:00+00') TO ('2026-02-01 00:00:00+00');

COMMENT ON TABLE market_facts_minute_202601 IS '分钟线分区：2026-01';

CREATE INDEX idx_market_facts_minute_202601_ts ON market_facts_minute_202601(ts);

-- 3) News (partitioned by month)
CREATE TABLE news_item (
                           news_id        UUID PRIMARY KEY,
                           source         TEXT NOT NULL,
                           source_item_id TEXT NOT NULL,
                           url            TEXT,
                           title          TEXT NOT NULL,
                           digest         TEXT,
                           content_ref    TEXT,
                           content_text   TEXT,
                           publish_time   TIMESTAMPTZ NOT NULL,
                           fetch_time     TIMESTAMPTZ NOT NULL DEFAULT now(),
                           language       TEXT NOT NULL DEFAULT 'zh',
                           author         TEXT,
                           hash           TEXT,
                           news_type      TEXT NOT NULL,          -- NEWS/ANNOUNCEMENT/REPORT...
                           extra          JSONB
) ;

COMMENT ON TABLE news_item IS '资讯/公告/研报等内容（按 publish_time 月分区）';
COMMENT ON COLUMN news_item.news_id IS '主键（UUID，建议代码生成或数据库生成）';
COMMENT ON COLUMN news_item.source IS '来源：eastmoney/cls/ths 等';
COMMENT ON COLUMN news_item.source_item_id IS '来源侧唯一ID（用于去重/溯源）';
COMMENT ON COLUMN news_item.url IS '原文链接';
COMMENT ON COLUMN news_item.title IS '标题';
COMMENT ON COLUMN news_item.digest IS '摘要';
COMMENT ON COLUMN news_item.content_ref IS '正文引用（对象存储/文件路径等）';
COMMENT ON COLUMN news_item.content_text IS '正文（小量可直接存，量大建议外置）';
COMMENT ON COLUMN news_item.publish_time IS '发布时间（建议UTC）';
COMMENT ON COLUMN news_item.fetch_time IS '抓取时间';
COMMENT ON COLUMN news_item.language IS '语言';
COMMENT ON COLUMN news_item.author IS '作者（可空）';
COMMENT ON COLUMN news_item.hash IS '内容hash（用于去重）';
COMMENT ON COLUMN news_item.news_type IS '资讯类型（由代码枚举控制）';
COMMENT ON COLUMN news_item.extra IS '扩展字段（JSON）';

-- CREATE TABLE news_item_202601
--     PARTITION OF news_item
--         FOR VALUES FROM ('2026-01-01 00:00:00+00') TO ('2026-02-01 00:00:00+00');
--
-- COMMENT ON TABLE news_item_202601 IS '资讯分区：2026-01';
--
-- CREATE INDEX idx_news_item_202601_publish_time ON news_item_202601(publish_time DESC);
-- CREATE INDEX idx_news_item_202601_source ON news_item_202601(source);

-- 你要全局去重（source, source_item_id）可以靠应用侧；
-- 如果你想数据库侧约束：需要额外建去重表（见下方 news_dedup）

CREATE TABLE news_security_map (
                                   news_id     UUID NOT NULL REFERENCES news_item(news_id) ON DELETE CASCADE,
                                   security_id BIGINT NOT NULL REFERENCES security(security_id) ON DELETE CASCADE,
                                   match_type  TEXT NOT NULL,          -- NER/RULE/SOURCE_TAG...
                                   confidence  NUMERIC(6,4) NOT NULL DEFAULT 0,
                                   created_time  TIMESTAMPTZ NOT NULL DEFAULT now(),
                                   PRIMARY KEY (news_id, security_id)
);

COMMENT ON TABLE news_security_map IS '资讯-证券关联（多对多）';
COMMENT ON COLUMN news_security_map.news_id IS '资讯ID';
COMMENT ON COLUMN news_security_map.security_id IS '证券ID';
COMMENT ON COLUMN news_security_map.match_type IS '关联方式（由代码枚举控制）';
COMMENT ON COLUMN news_security_map.confidence IS '关联置信度';
COMMENT ON COLUMN news_security_map.created_time IS '创建时间';

CREATE INDEX idx_news_security_map_security ON news_security_map(security_id);

CREATE TABLE news_feature (
                              news_id       UUID PRIMARY KEY REFERENCES news_item(news_id) ON DELETE CASCADE,
                              sentiment     NUMERIC(8,4),     -- -1~1
                              topic         TEXT,
                              importance    INT,
                              keywords      TEXT[],
                              model_version TEXT,
                              created_time    TIMESTAMPTZ NOT NULL DEFAULT now()
);

COMMENT ON TABLE news_feature IS '资讯衍生特征（情绪/主题/重要度/关键词等）';
COMMENT ON COLUMN news_feature.news_id IS '资讯ID';
COMMENT ON COLUMN news_feature.sentiment IS '情绪分值（-1~1，可空）';
COMMENT ON COLUMN news_feature.topic IS '主题分类（可空）';
COMMENT ON COLUMN news_feature.importance IS '重要度分数（可空）';
COMMENT ON COLUMN news_feature.keywords IS '关键词列表（可空）';
COMMENT ON COLUMN news_feature.model_version IS '模型版本（可空）';
COMMENT ON COLUMN news_feature.created_time IS '创建时间';

-- （可选）全局去重表：保证 (source, source_item_id) 唯一，不受分区影响
CREATE TABLE news_deduplicat (
                                 source         TEXT NOT NULL,
                                 source_item_id TEXT NOT NULL,
                                 news_id        UUID NOT NULL,
                                 created_time     TIMESTAMPTZ NOT NULL DEFAULT now(),
                                 PRIMARY KEY (source, source_item_id)
);

COMMENT ON TABLE news_deduplicat IS '资讯全局去重索引表（跨分区保证 source+source_item_id 唯一）';
COMMENT ON COLUMN news_deduplicat.source IS '来源';
COMMENT ON COLUMN news_deduplicat.source_item_id IS '来源侧唯一ID';
COMMENT ON COLUMN news_deduplicat.news_id IS '对应 news_item.news_id';
COMMENT ON COLUMN news_deduplicat.created_time IS '创建时间';

-- 4) Strategy / Factors / Signals
CREATE TABLE strategy (
                          strategy_id   BIGSERIAL PRIMARY KEY,
                          strategy_name          TEXT NOT NULL UNIQUE,
                          strategy_description   TEXT,
                          universe      TEXT NOT NULL DEFAULT 'ALL',
                          bar_type      TEXT NOT NULL DEFAULT '1D',   -- 1D/1M
                          params_schema JSONB,
                          code_ref      TEXT,
                          created_time    TIMESTAMPTZ NOT NULL DEFAULT now()
);

COMMENT ON TABLE strategy IS '策略定义（参数结构、代码版本、选股范围等）';
COMMENT ON COLUMN strategy.strategy_id IS '主键';
COMMENT ON COLUMN strategy.strategy_name IS '策略名称';
COMMENT ON COLUMN strategy.strategy_description IS '策略描述';
COMMENT ON COLUMN strategy.universe IS '股票池/范围定义（如ALL/HS300/自选等）';
COMMENT ON COLUMN strategy.bar_type IS '行情粒度：1D/1M';
COMMENT ON COLUMN strategy.params_schema IS '参数定义结构（JSON）';
COMMENT ON COLUMN strategy.code_ref IS '策略代码版本引用（git commit/tag等）';
COMMENT ON COLUMN strategy.created_time IS '创建时间';

CREATE TABLE strategy_run (
                              run_id        UUID PRIMARY KEY,
                              strategy_id   BIGINT NOT NULL REFERENCES strategy(strategy_id) ON DELETE CASCADE,
                              mode          TEXT NOT NULL,          -- BACKTEST/PAPER/LIVE...
                              start_time    TIMESTAMPTZ,
                              end_time      TIMESTAMPTZ,
                              params        JSONB,
                              data_snapshot JSONB,
                              status        TEXT NOT NULL DEFAULT 'RUNNING',
                              log_ref       TEXT,
                              created_time  TIMESTAMPTZ NOT NULL DEFAULT now()
);

COMMENT ON TABLE strategy_run IS '策略运行实例（回测/模拟/实盘一次运行）';
COMMENT ON COLUMN strategy_run.run_id IS '主键（UUID）';
COMMENT ON COLUMN strategy_run.strategy_id IS '策略ID';
COMMENT ON COLUMN strategy_run.mode IS '运行模式（由代码枚举控制）';
COMMENT ON COLUMN strategy_run.start_time IS '运行开始时间（可空）';
COMMENT ON COLUMN strategy_run.end_time IS '运行结束时间（可空）';
COMMENT ON COLUMN strategy_run.params IS '本次运行参数（JSON）';
COMMENT ON COLUMN strategy_run.data_snapshot IS '数据快照/版本信息（JSON）';
COMMENT ON COLUMN strategy_run.status IS '运行状态';
COMMENT ON COLUMN strategy_run.log_ref IS '日志引用（可空）';
COMMENT ON COLUMN strategy_run.created_time IS '创建时间';

CREATE INDEX idx_strategy_run_strategy_created ON strategy_run(strategy_id, created_time DESC);

CREATE TABLE factor_value (
                              factor_name  TEXT NOT NULL,
                              security_id  BIGINT NOT NULL REFERENCES security(security_id) ON DELETE CASCADE,
                              ts           TIMESTAMPTZ NOT NULL,
                              value        NUMERIC(30,12),
                              calc_version TEXT NOT NULL DEFAULT 'v1',
                              created_time   TIMESTAMPTZ NOT NULL DEFAULT now(),
                              PRIMARY KEY (factor_name, security_id, ts, calc_version)
);

COMMENT ON TABLE factor_value IS '因子/指标结果（长表，便于扩展与回放）';
COMMENT ON COLUMN factor_value.factor_name IS '因子名称（如MA20/RSI14/动量等）';
COMMENT ON COLUMN factor_value.security_id IS '证券ID';
COMMENT ON COLUMN factor_value.ts IS '对齐时间（与bar时间一致）';
COMMENT ON COLUMN factor_value.value IS '因子值';
COMMENT ON COLUMN factor_value.calc_version IS '计算版本（便于重算/对比）';
COMMENT ON COLUMN factor_value.created_time IS '创建时间';

CREATE INDEX idx_factor_value_security_ts ON factor_value(security_id, ts);

CREATE TABLE signal (
                        signal_id  BIGSERIAL PRIMARY KEY,
                        run_id     UUID NOT NULL REFERENCES strategy_run(run_id) ON DELETE CASCADE,
                        security_id BIGINT NOT NULL REFERENCES security(security_id) ON DELETE CASCADE,
                        ts         TIMESTAMPTZ NOT NULL,
                        signal_type TEXT NOT NULL,           -- BUY/SELL/HOLD/STOP/TAKE_PROFIT...
                        strength   INT NOT NULL DEFAULT 50,  -- 0~100
                        price_ref  NUMERIC(18,6),
                        reason     TEXT,
                        meta       JSONB,
                        created_time TIMESTAMPTZ NOT NULL DEFAULT now(),
                        UNIQUE (run_id, security_id, ts, signal_type)
);

COMMENT ON TABLE signal IS '策略信号（买卖/止损/止盈等）';
COMMENT ON COLUMN signal.signal_id IS '主键';
COMMENT ON COLUMN signal.run_id IS '策略运行ID';
COMMENT ON COLUMN signal.security_id IS '证券ID';
COMMENT ON COLUMN signal.ts IS '信号时间';
COMMENT ON COLUMN signal.signal_type IS '信号类型（由代码枚举控制）';
COMMENT ON COLUMN signal.strength IS '信号强度（0~100）';
COMMENT ON COLUMN signal.price_ref IS '触发参考价（可空）';
COMMENT ON COLUMN signal.reason IS '触发原因（可空）';
COMMENT ON COLUMN signal.meta IS '扩展信息（JSON）';
COMMENT ON COLUMN signal.created_time IS '创建时间';

CREATE INDEX idx_signal_run_ts ON signal(run_id, ts);
CREATE INDEX idx_signal_security_ts ON signal(security_id, ts);

CREATE TABLE position_target (
                                 run_id        UUID NOT NULL REFERENCES strategy_run(run_id) ON DELETE CASCADE,
                                 ts            TIMESTAMPTZ NOT NULL,
                                 security_id   BIGINT NOT NULL REFERENCES security(security_id) ON DELETE CASCADE,
                                 target_weight NUMERIC(10,6) NOT NULL,
                                 target_shares BIGINT,
                                 comment       TEXT,
                                 PRIMARY KEY (run_id, ts, security_id)
);

COMMENT ON TABLE position_target IS '策略输出的目标仓位（组合权重/目标股数）';
COMMENT ON COLUMN position_target.run_id IS '策略运行ID';
COMMENT ON COLUMN position_target.ts IS '目标时间';
COMMENT ON COLUMN position_target.security_id IS '证券ID';
COMMENT ON COLUMN position_target.target_weight IS '目标权重（0~1）';
COMMENT ON COLUMN position_target.target_shares IS '目标股数（可空）';
COMMENT ON COLUMN position_target.comment IS '备注（可空）';

CREATE INDEX idx_position_target_ts ON position_target(ts);

-- 5) Trading
CREATE TABLE account (
                         account_id    BIGSERIAL PRIMARY KEY,
                         broker        TEXT NOT NULL,            -- PAPER/券商名
                         mode          TEXT NOT NULL,            -- PAPER/LIVE...
                         base_currency TEXT NOT NULL DEFAULT 'CNY',
                         created_time    TIMESTAMPTZ NOT NULL DEFAULT now()
);

COMMENT ON TABLE account IS '账户（模拟/实盘）';
COMMENT ON COLUMN account.account_id IS '主键';
COMMENT ON COLUMN account.broker IS '券商/通道标识（模拟也可）';
COMMENT ON COLUMN account.mode IS '账户模式（由代码枚举控制）';
COMMENT ON COLUMN account.base_currency IS '基准币种';
COMMENT ON COLUMN account.created_time IS '创建时间';

CREATE TABLE orders (
                        order_id    UUID PRIMARY KEY,
                        account_id  BIGINT NOT NULL REFERENCES account(account_id) ON DELETE CASCADE,
                        run_id      UUID REFERENCES strategy_run(run_id) ON DELETE SET NULL,
                        security_id BIGINT NOT NULL REFERENCES security(security_id),
                        ts          TIMESTAMPTZ NOT NULL DEFAULT now(),
                        side        TEXT NOT NULL,              -- BUY/SELL...
                        order_type  TEXT NOT NULL,              -- LIMIT/MARKET...
                        price       NUMERIC(18,6),
                        qty         BIGINT NOT NULL,
                        status      TEXT NOT NULL DEFAULT 'NEW',
                        source      TEXT NOT NULL DEFAULT 'STRATEGY', -- STRATEGY/MANUAL
                        reason      TEXT,
                        extra       JSONB
);

COMMENT ON TABLE orders IS '订单（策略或手动下单）';
COMMENT ON COLUMN orders.order_id IS '主键（UUID）';
COMMENT ON COLUMN orders.account_id IS '账户ID';
COMMENT ON COLUMN orders.run_id IS '策略运行ID（可空）';
COMMENT ON COLUMN orders.security_id IS '证券ID';
COMMENT ON COLUMN orders.ts IS '下单时间';
COMMENT ON COLUMN orders.side IS '买卖方向（由代码枚举控制）';
COMMENT ON COLUMN orders.order_type IS '订单类型（由代码枚举控制）';
COMMENT ON COLUMN orders.price IS '委托价（市价单可空）';
COMMENT ON COLUMN orders.qty IS '委托数量';
COMMENT ON COLUMN orders.status IS '订单状态（由代码枚举控制）';
COMMENT ON COLUMN orders.source IS '订单来源：STRATEGY/MANUAL 等';
COMMENT ON COLUMN orders.reason IS '下单原因（可空）';
COMMENT ON COLUMN orders.extra IS '扩展信息（JSON）';

CREATE INDEX idx_orders_account_ts ON orders(account_id, ts DESC);
CREATE INDEX idx_orders_security_ts ON orders(security_id, ts DESC);
CREATE INDEX idx_orders_run_ts ON orders(run_id, ts DESC);

CREATE TABLE trade_fill (
                            fill_id           UUID PRIMARY KEY,
                            order_id          UUID NOT NULL REFERENCES orders(order_id) ON DELETE CASCADE,
                            ts                TIMESTAMPTZ NOT NULL,
                            price             NUMERIC(18,6) NOT NULL,
                            qty               BIGINT NOT NULL,
                            fee               NUMERIC(18,6) NOT NULL DEFAULT 0,
                            tax               NUMERIC(18,6) NOT NULL DEFAULT 0,
                            exchange_trade_id TEXT,
                            extra             JSONB
);

COMMENT ON TABLE trade_fill IS '成交回报/撮合结果（一笔订单可多笔成交）';
COMMENT ON COLUMN trade_fill.fill_id IS '主键（UUID）';
COMMENT ON COLUMN trade_fill.order_id IS '订单ID';
COMMENT ON COLUMN trade_fill.ts IS '成交时间';
COMMENT ON COLUMN trade_fill.price IS '成交价';
COMMENT ON COLUMN trade_fill.qty IS '成交量';
COMMENT ON COLUMN trade_fill.fee IS '手续费';
COMMENT ON COLUMN trade_fill.tax IS '税费';
COMMENT ON COLUMN trade_fill.exchange_trade_id IS '交易所成交编号（可空）';
COMMENT ON COLUMN trade_fill.extra IS '扩展信息（JSON）';

CREATE INDEX idx_trade_fill_order_ts ON trade_fill(order_id, ts);

CREATE TABLE position_snapshot (
                                   account_id     BIGINT NOT NULL REFERENCES account(account_id) ON DELETE CASCADE,
                                   ts             TIMESTAMPTZ NOT NULL,
                                   security_id    BIGINT NOT NULL REFERENCES security(security_id),
                                   qty            BIGINT NOT NULL,
                                   avg_cost       NUMERIC(18,6),
                                   market_price   NUMERIC(18,6),
                                   market_value   NUMERIC(20,2),
                                   unrealized_pnl NUMERIC(20,2),
                                   realized_pnl   NUMERIC(20,2),
                                   PRIMARY KEY (account_id, ts, security_id)
);

COMMENT ON TABLE position_snapshot IS '持仓快照（建议日终/分钟，用于回测与风控）';
COMMENT ON COLUMN position_snapshot.account_id IS '账户ID';
COMMENT ON COLUMN position_snapshot.ts IS '快照时间';
COMMENT ON COLUMN position_snapshot.security_id IS '证券ID';
COMMENT ON COLUMN position_snapshot.qty IS '持仓数量';
COMMENT ON COLUMN position_snapshot.avg_cost IS '持仓均价（可空）';
COMMENT ON COLUMN position_snapshot.market_price IS '市价（可空）';
COMMENT ON COLUMN position_snapshot.market_value IS '市值（可空）';
COMMENT ON COLUMN position_snapshot.unrealized_pnl IS '浮动盈亏（可空）';
COMMENT ON COLUMN position_snapshot.realized_pnl IS '已实现盈亏（可空）';

CREATE INDEX idx_position_snapshot_security_ts ON position_snapshot(security_id, ts);

CREATE TABLE cash_snapshot (
                               account_id   BIGINT NOT NULL REFERENCES account(account_id) ON DELETE CASCADE,
                               ts           TIMESTAMPTZ NOT NULL,
                               cash         NUMERIC(20,2) NOT NULL,
                               frozen_cash  NUMERIC(20,2) NOT NULL DEFAULT 0,
                               equity       NUMERIC(20,2) NOT NULL,
                               drawdown     NUMERIC(12,6),
                               PRIMARY KEY (account_id, ts)
);

COMMENT ON TABLE cash_snapshot IS '资金快照（资产、可用资金、回撤等）';
COMMENT ON COLUMN cash_snapshot.account_id IS '账户ID';
COMMENT ON COLUMN cash_snapshot.ts IS '快照时间';
COMMENT ON COLUMN cash_snapshot.cash IS '可用现金';
COMMENT ON COLUMN cash_snapshot.frozen_cash IS '冻结资金';
COMMENT ON COLUMN cash_snapshot.equity IS '账户总权益';
COMMENT ON COLUMN cash_snapshot.drawdown IS '回撤（可空）';

CREATE TABLE risk_event (
                            event_id   BIGSERIAL PRIMARY KEY,
                            account_id BIGINT NOT NULL REFERENCES account(account_id) ON DELETE CASCADE,
                            ts         TIMESTAMPTZ NOT NULL DEFAULT now(),
                            risk_type  TEXT NOT NULL,
                            detail     JSONB
);

COMMENT ON TABLE risk_event IS '风控事件（触发止损/超限/异常等）';
COMMENT ON COLUMN risk_event.event_id IS '主键';
COMMENT ON COLUMN risk_event.account_id IS '账户ID';
COMMENT ON COLUMN risk_event.ts IS '事件时间';
COMMENT ON COLUMN risk_event.risk_type IS '事件类型（由代码枚举控制）';
COMMENT ON COLUMN risk_event.detail IS '事件详情（JSON）';

CREATE INDEX idx_risk_event_account_ts ON risk_event(account_id, ts DESC);

-- 6) Ingest / Source (Optional but recommended)
CREATE TABLE data_source (
                             source_id   BIGSERIAL PRIMARY KEY,
                             name        TEXT NOT NULL UNIQUE,   -- eastmoney/tushare/broker_api...
                             source_type TEXT NOT NULL,          -- HTTP/API/FILE...
                             auth_type   TEXT,
                             rate_limit  INT,
                             notes       TEXT,
                             created_time  TIMESTAMPTZ NOT NULL DEFAULT now()
);

COMMENT ON TABLE data_source IS '数据源定义（便于扩展/溯源/限流管理）';
COMMENT ON COLUMN data_source.source_id IS '主键';
COMMENT ON COLUMN data_source.name IS '数据源名称';
COMMENT ON COLUMN data_source.source_type IS '数据源类型';
COMMENT ON COLUMN data_source.auth_type IS '鉴权类型（可空）';
COMMENT ON COLUMN data_source.rate_limit IS '限流（可空）';
COMMENT ON COLUMN data_source.notes IS '备注（可空）';
COMMENT ON COLUMN data_source.created_time IS '创建时间';

CREATE TABLE ingest_job (
                            job_id         BIGSERIAL PRIMARY KEY,
                            source_id      BIGINT NOT NULL REFERENCES data_source(source_id) ON DELETE CASCADE,
                            job_type       TEXT NOT NULL,       -- SECURITY_LIST/market_daily/BAR_1M/NEWS...
                            schedule_cron  TEXT,
                            status         TEXT NOT NULL DEFAULT 'ENABLED',
                            last_run_time  TIMESTAMPTZ,
                            last_success_time TIMESTAMPTZ,
                            created_time     TIMESTAMPTZ NOT NULL DEFAULT now(),
                            UNIQUE (source_id, job_type)
);

COMMENT ON TABLE ingest_job IS '采集任务定义（定时/增量/全量）';
COMMENT ON COLUMN ingest_job.job_id IS '主键';
COMMENT ON COLUMN ingest_job.source_id IS '数据源ID';
COMMENT ON COLUMN ingest_job.job_type IS '任务类型（由代码枚举控制）';
COMMENT ON COLUMN ingest_job.schedule_cron IS 'cron表达式（可空）';
COMMENT ON COLUMN ingest_job.status IS '任务状态';
COMMENT ON COLUMN ingest_job.last_run_time IS '上次运行时间（可空）';
COMMENT ON COLUMN ingest_job.last_success_time IS '上次成功时间（可空）';
COMMENT ON COLUMN ingest_job.created_time IS '创建时间';

CREATE TABLE ingest_log (
                            log_id      BIGSERIAL PRIMARY KEY,
                            job_id      BIGINT NOT NULL REFERENCES ingest_job(job_id) ON DELETE CASCADE,
                            start_time  TIMESTAMPTZ NOT NULL DEFAULT now(),
                            end_time    TIMESTAMPTZ,
                            status      TEXT NOT NULL,          -- SUCCESS/FAILED
                            records_in  BIGINT,
                            records_out BIGINT,
                            error_msg   TEXT,
                            checkpoint  JSONB
);

COMMENT ON TABLE ingest_log IS '采集运行日志（用于排错/补数/监控）';
COMMENT ON COLUMN ingest_log.log_id IS '主键';
COMMENT ON COLUMN ingest_log.job_id IS '任务ID';
COMMENT ON COLUMN ingest_log.start_time IS '开始时间';
COMMENT ON COLUMN ingest_log.end_time IS '结束时间（可空）';
COMMENT ON COLUMN ingest_log.status IS '运行状态';
COMMENT ON COLUMN ingest_log.records_in IS '输入记录数（可空）';
COMMENT ON COLUMN ingest_log.records_out IS '输出记录数（可空）';
COMMENT ON COLUMN ingest_log.error_msg IS '错误信息（可空）';
COMMENT ON COLUMN ingest_log.checkpoint IS '增量检查点（JSON，可空）';

CREATE INDEX idx_ingest_log_job_start_time ON ingest_log(job_id, start_time DESC);

CREATE TABLE raw_payload (
                             payload_id  UUID PRIMARY KEY,
                             source_id   BIGINT NOT NULL REFERENCES data_source(source_id) ON DELETE CASCADE,
                             entity_type TEXT NOT NULL,
                             entity_key  TEXT NOT NULL,
                             fetch_time  TIMESTAMPTZ NOT NULL DEFAULT now(),
                             payload     JSONB
);

COMMENT ON TABLE raw_payload IS '原始数据载荷（便于溯源与重放解析；量大可只存引用）';
COMMENT ON COLUMN raw_payload.payload_id IS '主键（UUID）';
COMMENT ON COLUMN raw_payload.source_id IS '数据源ID';
COMMENT ON COLUMN raw_payload.entity_type IS '实体类型（market_daily/NEWS等，由代码枚举控制）';
COMMENT ON COLUMN raw_payload.entity_key IS '实体键（如security_id|date等）';
COMMENT ON COLUMN raw_payload.fetch_time IS '抓取时间';
COMMENT ON COLUMN raw_payload.payload IS '原始payload（JSON）';

CREATE INDEX idx_raw_payload_source_fetch_time ON raw_payload(source_id, fetch_time DESC);
CREATE INDEX idx_raw_payload_entity ON raw_payload(entity_type, entity_key);
