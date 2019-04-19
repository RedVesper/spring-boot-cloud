package cn.zhangxd.svcb.services.impl;

import cn.zhangxd.svcb.services.IUserLoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class UserLoginService implements IUserLoin {

    @Override
    public boolean UserLogin(String userName, String userPassWord){


        List<String> list= new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            list.add(i + ",");
        }

        int cpuPoolSize = Runtime.getRuntime().availableProcessors();
        ExecutorService  executorService= Executors.newFixedThreadPool(cpuPoolSize);
        List<Future<String>> result = new ArrayList<Future<String>>();

        StringBuffer ret = new StringBuffer();
        int size = list.size();
        for (int i = 0; i < cpuPoolSize; i++) {
            final List<String> subList = list.subList(size / cpuPoolSize * i, size/ cpuPoolSize * (i + 1));
            Callable<String> task = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    StringBuffer sb = new StringBuffer();
                    for (String str : subList) {
                        sb.append(str);
                    }
                    return sb.toString();
                }
            };
            result.add(executorService.submit(task));
        }

        for (Future<String> future : result) {
            try {
                ret.append(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();

        return  true;

    }
}
