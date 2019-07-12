update eg_wf_matrix set validactions ='Approve,Revert,Reject,Forward to Overseer,Forward to Superintendent,Forward to Secretary' where fromqty=1501 and toqty=2500 and nextstate='Final Approval Process initiated' and nextaction='Permit Fee Collection Pending' and objecttype ='BpaApplication' and additionalrule='CREATEBPAAPPLICATION';

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate)
 VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'BpaApplication', 'Corporation Engineer Application Approval Pending', 'Noc Updated', 'Forwarding to secretary is pending', 'Corporation Engineer', 'CREATEBPAAPPLICATION', 'Corporation Engineer forwarded to secretary reviewal', 'Secretary approval pending', 'Secretary', 'NOC Updated', 'Forward', 1501, 2500, now(), '2099-04-01');


INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate)
 VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'BpaApplication', 'Corporation Engineer forwarded to secretary reviewal', 'Noc Updated', 'Secretary approval pending', 'Secretary', 'CREATEBPAAPPLICATION', 'Application reviewal completed', 'Corporation Engineer approval pending', 'Corporation Engineer', 'NOC Updated', 'Forward', 1501, 2500, now(), '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate)
 VALUES (nextval('seq_eg_wf_matrix'), 'ANY', 'BpaApplication', 'Application reviewal completed', 'Noc Updated', 'Corporation Engineer approval pending', 'Corporation Engineer', 'CREATEBPAAPPLICATION', 'Final Approval Process initiated', 'Permit Fee Collection Pending', 'Superintendent', 'Approved', 'Approve,Reject,Forward to Overseer,Forward to Superintendent,Forward to Secretary', 1501, 2500, now(), '2099-04-01');