/*
 * BinUtils - access various binary formats from Java
 *
 * (C) Copyright 2016 - JaWi - j.w.janssen@lxtreme.nl
 *
 * Licensed under Apache License v2. 
 */
package nl.lxtreme.binutils.elf;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Represents information about the various segments in an ELF object.
 */
public class ProgramHeader {
	public final SegmentType type;
	public final long flags;
	public final long offset;
	public final long virtualAddress;
	public final long offsetOfVirtualAddress;
	public final long physicalAddress;
	public final long offsetOfPhysicalAddress;
	public final long segmentFileSize;
	public final long offsetOfSegmentFileSize;
	public final long segmentMemorySize;
	public final long offsetOfSegmentMemorySize;
	public final long segmentAlignment;

	public ProgramHeader(ElfClass elfClass, ByteBuffer buf, long programHeaderOffset) throws IOException {
		switch (elfClass) {
		case CLASS_32:
			type = SegmentType.valueOf(buf.getInt() & 0xFFFFFFFF);
			offset = buf.getInt() & 0xFFFFFFFFL;
			virtualAddress = buf.getInt() & 0xFFFFFFFFL;
			offsetOfVirtualAddress = programHeaderOffset + buf.position() - Integer.BYTES;
			physicalAddress = buf.getInt() & 0xFFFFFFFFL;
			offsetOfPhysicalAddress = programHeaderOffset + buf.position() - Integer.BYTES;
			segmentFileSize = buf.getInt() & 0xFFFFFFFFL;
			offsetOfSegmentFileSize = programHeaderOffset + buf.position() - Integer.BYTES;
			segmentMemorySize = buf.getInt() & 0xFFFFFFFFL;
			offsetOfSegmentMemorySize = programHeaderOffset + buf.position() - Integer.BYTES;
			flags = buf.getInt() & 0xFFFFFFFFL;
			segmentAlignment = buf.getInt() & 0xFFFFFFFFL;
			break;
		case CLASS_64:
			type = SegmentType.valueOf(buf.getInt() & 0xFFFFFFFF);
			flags = buf.getInt() & 0xFFFFFFFFL;
			offset = buf.getLong();
			virtualAddress = buf.getLong();
			offsetOfVirtualAddress = programHeaderOffset + buf.position() - Long.BYTES;
			physicalAddress = buf.getLong();
			offsetOfPhysicalAddress = programHeaderOffset + buf.position() - Long.BYTES;
			segmentFileSize = buf.getLong();
			offsetOfSegmentFileSize = programHeaderOffset + buf.position() - Long.BYTES;
			segmentMemorySize = buf.getLong();
			offsetOfSegmentMemorySize = programHeaderOffset + buf.position() - Long.BYTES;
			segmentAlignment = buf.getLong();
			break;
		default:
			throw new IOException("Unhandled ELF-class!");
		}
	}
}