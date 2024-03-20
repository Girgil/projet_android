package com.example.projet_android.commandes;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.projet_android.MainImage;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    private static CommandManager instance;

    private final List<ICommand> cmdHistory;

    private final List<ICommand> undoCmdHistory;

    private CommandManager() {
        this.cmdHistory = new ArrayList<>();
        this.undoCmdHistory = new ArrayList<>();
    }

    public static CommandManager getInstance() {
        if (instance == null) {
            instance = new CommandManager();
        }
        return instance;
    }

    public void addCommand(ICommand cmd) {
        this.cmdHistory.add(cmd);
        this.undoCmdHistory.clear();
    }

    public ICommand getLastCommand(List<ICommand> list) {
        if (!list.isEmpty()) {
            return list.remove(list.size() -1);
        }
        return null;
    }

    public List<ICommand> getCmdHistory() {
        return cmdHistory;
    }

    public void undoLastCommand(MainImage img) {
        ICommand cmd = this.getLastCommand(this.cmdHistory);
        if (cmd != null) {
            cmd.undo(img);
            this.undoCmdHistory.add(cmd);
        }
    }

    public void redoCommand(MainImage img) {
        ICommand cmd = this.getLastCommand(this.undoCmdHistory);
        if (cmd != null) {
            cmd.redo(img);
            this.cmdHistory.add(cmd);
        }
        Log.d("mes logs", "cmd : " + this.cmdHistory.size() + " undoCmd" + this.undoCmdHistory.size());
    }

    @NonNull
    @Override
    public String toString() {
        return "GestionnaireCommande{" +
                "cmdHistory=" + cmdHistory +
                '}';
    }
}
