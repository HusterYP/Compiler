package util;

import slp.Main;
import slp.Slp;

import java.util.HashMap;
import java.util.Map;

// 求print语句的最多参数个数, 采用递归的思想
public class Todo {
    private Main main = new Main();
    private Map<String, Integer> map = new HashMap<>();

    public Todo() {
//        System.out.println("TODO: please add your code here:\n");
//        throw new java.lang.Error();
    }

    ////////////////////////////////////////
    // 求Print语句的最多参数
    ////////////////////////////////////////
    public int maxArgsAssign(Slp.Stm.Assign assign) {
        if (assign.exp instanceof Slp.Exp.Op) {
            int n1 = maxArgsExp(((Slp.Exp.Op) assign.exp).left);
            int n2 = maxArgsExp(((Slp.Exp.Op) assign.exp).right);
            return Math.max(n1, n2);
        } else if (assign.exp instanceof Slp.Exp.Eseq) {
            int n1 = main.maxArgsStm(((Slp.Exp.Eseq) assign.exp).stm);
            int n2 = maxArgsExp(((Slp.Exp.Eseq) assign.exp).exp);
            return Math.max(n1, n2);
        }
        return 0;
    }

    private int maxArgsExp(Slp.Exp.T t) {
        if (t instanceof Slp.Exp.Eseq) {
            if (((Slp.Exp.Eseq) t).stm instanceof Slp.Stm.Print) {
                maxArgsPrint((Slp.Stm.Print) ((Slp.Exp.Eseq) t).stm);
            }
            return main.maxArgsStm(((Slp.Exp.Eseq) t).stm);
        }
        return 0;
    }

    public int maxArgsPrint(Slp.Stm.Print print) {
        if (print.explist instanceof Slp.ExpList.Last) {
            return 1;
        } else if (print.explist instanceof Slp.ExpList.Pair) {
            int n = 1;
            n += maxArgsPrint(((Slp.ExpList.Pair) print.explist).list);
            return n;
        }
        new Bug();
        return 0;
    }

    public int maxArgsPrint(Slp.ExpList.T t) {
        if (t instanceof Slp.ExpList.Last)
            return 1;
        else if (t instanceof Slp.ExpList.Pair) {
            int n = 1;
            n += maxArgsPrint(((Slp.ExpList.Pair) t).list);
            return n;
        }
        new Bug();
        return 0;
    }

    ////////////////////////////////////////
    // 解释器: 写崩了 :<(
    ////////////////////////////////////////
    public void interpCompound(Slp.Stm.Compound compound) {
        if (compound.s1 instanceof Slp.Stm.Compound)
            interpCompound((Slp.Stm.Compound) compound.s1);
        else if (compound.s1 instanceof Slp.Stm.Assign)
            interpAssign((Slp.Stm.Assign) compound.s1);
        else if (compound.s1 instanceof Slp.Stm.Print)
            interpPrint((Slp.Stm.Print) compound.s1);
        else
            new Bug();

        if (compound.s2 instanceof Slp.Stm.Compound)
            interpCompound((Slp.Stm.Compound) compound.s2);
        else if (compound.s2 instanceof Slp.Stm.Assign)
            interpAssign((Slp.Stm.Assign) compound.s2);
        else if (compound.s2 instanceof Slp.Stm.Print)
            interpPrint((Slp.Stm.Print) compound.s2);
        else
            new Bug();
    }

    public int interpAssign(Slp.Stm.Assign assign) {
        String id = assign.id;
        int result = 0;
        if (assign.exp instanceof Slp.Exp.Num) {
            result = ((Slp.Exp.Num) assign.exp).num;
        } else if (assign.exp instanceof Slp.Exp.Op) {
            result = interpOp((Slp.Exp.Op) assign.exp);
        } else if (assign.exp instanceof Slp.Exp.Eseq) {
            result = interpEseq((Slp.Exp.Eseq) assign.exp);
        } else
            new Bug();
        map.put(id, result);
        return result;
    }

    private int interpOp(Slp.Exp.T op) {
        if (op instanceof Slp.Exp.Op) {
            Slp.Exp.Op opt = (Slp.Exp.Op) op;
            if (opt.left instanceof Slp.Exp.Num) {
                int left = ((Slp.Exp.Num) opt.left).num;
                int right = interpOp(opt.right);
                return computeResult(left, right, opt.op);
            } else if (opt.left instanceof Slp.Exp.Id) {
                Integer left = map.get(((Slp.Exp.Id) opt.left).id);
                if (left == null) {
                    new Bug();
                    return 0;
                }
                int right = interpOp(opt.right);
                return computeResult(left, right, opt.op);
            }

            if (opt.right instanceof Slp.Exp.Num) {
                return ((Slp.Exp.Num) opt.right).num;
            } else if (opt.right instanceof Slp.Exp.Op) {
                return interpOp(opt.right);
            } else if (opt.right instanceof Slp.Exp.Eseq) {
                return interpEseq((Slp.Exp.Eseq) opt.right);
            } else
                new Bug();
        }
        return 0;
    }

    private int computeResult(int left, int right, Slp.Exp.OP_T op) {
        switch (op) {
            case ADD:
                return left + right;
            case SUB:
                return left - right;
            case TIMES:
                return left * right;
            case DIVIDE: {
                if (right == 0)
                    new Bug();
                return left / right;
            }
            default:
                new Bug();
        }
        return 0;
    }

    private int interpEseq(Slp.Exp.Eseq eseq) {
        if (eseq.stm instanceof Slp.Stm.Print) {
            return interpPrint((Slp.Stm.Print) eseq.stm);
        } else
            new Bug();

        if (eseq.exp instanceof Slp.Exp.Op) {
            return interpOp((Slp.Exp.Op) eseq.exp);
        } else if (eseq.exp instanceof Slp.Exp.Eseq) {
            return interpEseq((Slp.Exp.Eseq) eseq.exp);
        } else if (eseq.exp instanceof Slp.Exp.Num) {
            return ((Slp.Exp.Num) eseq.exp).num;
        } else
            new Bug();
        return 0;
    }

    public int interpPrint(Slp.Stm.Print print) {
        if (print.explist instanceof Slp.ExpList.Last) {
            int result = 0;
            if (((Slp.ExpList.Last) print.explist).exp instanceof Slp.Exp.Op) {
                result = interpOp((Slp.Exp.Op) ((Slp.ExpList.Last) print.explist).exp);
            } else if (((Slp.ExpList.Last) print.explist).exp instanceof Slp.Exp.Num) {
                result = ((Slp.Exp.Num) ((Slp.ExpList.Last) print.explist).exp).num;
            } else if (((Slp.ExpList.Last) print.explist).exp instanceof Slp.Exp.Eseq) {
                result = interpEseq((Slp.Exp.Eseq) ((Slp.ExpList.Last) print.explist).exp);
            }
            System.out.print(result + "\t");
            return result;
        } else if (print.explist instanceof Slp.ExpList.Pair) {
            return interpPrint((Slp.ExpList.Pair) print.explist);
        } else
            new Bug();
        return 0;
    }

    private int interpPrint(Slp.ExpList.Pair print) {
        Integer result = null;
        if (print.exp instanceof Slp.Exp.Id) {
            result = map.get(((Slp.Exp.Id) print.exp).id);
            if (result == null) {
                new Bug();
                return 0;
            }
        } else if (print.exp instanceof Slp.Exp.Num) {
            result = ((Slp.Exp.Num) print.exp).num;
        } else if (print.exp instanceof Slp.Exp.Op) {
            result = interpOp((Slp.Exp.Op) print.exp);
        } else if (print.exp instanceof Slp.Exp.Eseq) {
            result = interpEseq((Slp.Exp.Eseq) print.exp);
        } else
            new Bug();
        System.out.print(result + "\t");

        if (print.list instanceof Slp.ExpList.Pair) {
            interpPrint((Slp.ExpList.Pair) print.list);
        }
        return result;
    }
}
