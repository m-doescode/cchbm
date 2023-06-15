package tech.eglspace.majdstuff.mods.cchbm;

import net.minecraft.nbt.*;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class LuaHelper {
    // Primitive arrays

//    private static LuaTable convertNbtByteArray(NBTTagByteArray source) {
//        LuaTable table = new LuaTable();
//        for (byte b : source.getByteArray()) {
//            table.add((double)b);
//        }
//        return table;
//    }
//
//    private static LuaTable convertNbtIntArray(NBTTagIntArray source) {
//        LuaTable table = new LuaTable();
//        for (int i : source.getIntArray()) {
//            table.add((int)i);
//        }
//        return table;
//    }
//
//    private static LuaTable convertNbtList(NBTTagList source) {
//        LuaTable table = new LuaTable();
//        for (NBTBase nbtValue : source) {
//            table.add(convertNbtToLua(nbtValue));
//        }
//        return table;
//    }
//
//    public static void copyNbtToTable(NBTTagCompound source, LuaTable destination) {
//        for (String key : source.getKeySet()) {
//            NBTBase nbtValue = source.getTag(key);
//            destination.set(key, convertNbtToLua(nbtValue));
//        }
//    }
//
//    public static LuaTable convertNbtTable(NBTTagCompound source) {
//        LuaTable table = new LuaTable();
//        for (String key : source.getKeySet()) {
//            NBTBase nbtValue = source.getTag(key);
//            table.set(key, convertNbtToLua(nbtValue));
//        }
//        return table;
//    }

    public static @Nullable Object convertNbtToCC(@Nullable NBTBase nbtValue) {
        if (nbtValue == null)
            return null;
        switch (nbtValue.getId()) {
            // Integer numbers
            case 1:
            case 2:
            case 3:
            case 4:
                return ((NBTPrimitive) nbtValue).getInt();
            // Floating-point numbers
            case 5:
            case 6:
                return ((NBTPrimitive) nbtValue).getDouble();
            // Byte array
            case 7:
                return ((NBTTagByteArray) nbtValue).getByteArray();
            // String
            case 8:
                return ((NBTTagString)nbtValue).getString();
            // List
            case 9:
                return mapify(StreamSupport.stream(((NBTTagList)nbtValue).spliterator(), false).collect(Collectors.toList()));
            // Compound
            case 10:
                // This is fucking insane. Straight up.
                return ((NBTTagCompound)nbtValue).getKeySet().stream().map(key -> new AbstractMap.SimpleEntry<>(key, convertNbtToCC(((NBTTagCompound) nbtValue).getTag(key)))).filter(entry -> entry.getValue() != null).collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
            // Int array
            case 11:
                return Arrays.stream(((NBTTagIntArray) nbtValue).getIntArray()).boxed().collect(Collectors.toList());
            // Long array
            case 12:
                // Long arrays seem not to have any function. They are not supported.
                return null;
            default:
                return null;
        }
    }

    // I hate ComputerCraft
    public static Map<Integer, ?> mapify(List<?> objects, boolean startAtZero) {
        Map<Integer, Object> map = new HashMap<>();
        for (int i = 0; i < objects.size(); i++) {
            map.put(startAtZero ? i : i + 1, objects.get(i));
        }
        return map;
    }

    public static Map<Integer, ?> mapify(List<?> objects) {
        return mapify(objects, false);
    }
}
