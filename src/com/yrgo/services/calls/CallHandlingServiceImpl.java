package com.yrgo.services.calls;

import com.yrgo.domain.Action;
import com.yrgo.domain.Call;
import com.yrgo.services.customers.CustomerManagementService;
import com.yrgo.services.customers.CustomerNotFoundException;
import com.yrgo.services.diary.DiaryManagementService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Transactional
public class CallHandlingServiceImpl implements CallHandlingService{
    private DiaryManagementService dms;
    private CustomerManagementService cms;

    public CallHandlingServiceImpl(DiaryManagementService dms, CustomerManagementService cms) {
        this.dms = dms;
        this.cms = cms;
    }

    @Override
    public void recordCall(String customerId, Call newCall, Collection<Action> actions) throws CustomerNotFoundException {
        cms.recordCall(customerId, newCall);
        for (Action action:actions
             ) {
            dms.recordAction(action);
        }
    }
}
