update eg_demand_reason set glcodeid =(select id from chartofaccounts where glcode='1401101') where ID_DEMAND_REASON_MASTER=(select id from eg_demand_reason_master where reasonmaster='License Fee' and module=(select id from eg_module where name='Trade License')) and ID_INSTALLMENT=(select id from EG_INSTALLMENT_MASTER where ID_MODULE = (select id from EG_MODULE where name = 'Trade License') and start_date = to_date('01/04/2017 00:00:00','dd/MM/yyyy HH24:MI:SS'));

update eg_demand_reason set glcodeid =(select id from chartofaccounts where glcode='1402001') where ID_DEMAND_REASON_MASTER=(select id from eg_demand_reason_master where reasonmaster='Penalty' and module=(select id from eg_module where name='Trade License')) and ID_INSTALLMENT=(select id from EG_INSTALLMENT_MASTER where ID_MODULE = (select id from EG_MODULE where name = 'Trade License') and start_date = to_date('01/04/2017 00:00:00','dd/MM/yyyy HH24:MI:SS'));

