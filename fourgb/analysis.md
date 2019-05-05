### Some general insights

- 4 billion, different, 32-bit integers only a slightly less, then the maximum number of all such integers (2^32-1) (it is _very_ suspicious)
- to find _any_ integer that is not included in the file (in the case of an unordered file), we must somehow maintain some knowledge about the full content of the file

### Solution one (brute-force bitset)

Just read the through the whole file. Whenever an integer is read, set the corresponding bit (whose index is the position of the integer in the file) in a 2^32-element bitset.

At the end of the run, all bits those remain unset represent a number _not_ in the file. These numbers can be determined via the sequential scan of the bitset.

#### Memory
The size of the bitset required for this solution is 4Gb/8 = 512 Mb. 

#### Disk
As we like it. With buffered reading (with a large enough buffer) the file can be read in just few passes. With a smaller buffer, the number of necessary disk operations will be higher. Rule of thumb: 4Gb/buffer size is the number of disk reads.

#### Remark
In this special case, this structure is the _compressed_ version of the file.

### Solution two (divide and conquer)

Let's divide the [0,2^32-1] interval into n (= 2,4,...) equal-sized sub-intervals. Read through the file, and count the _number_ of integers in the respective sub-intervals. We need only n integers to do that. Due to the pigeonhole principle, there is guaranteed that at least one variable is going to have less value, than the possible maximum, 2^32/n. 

So we can identify an _interval_ which has a number that is not in the file. In the second reading pass, like in solution one, we can identify that (those) integer(s), by using a smaller sized bitset (2^32/8n). During the second pass we should just pay attention to the integers falling into the respective interval, and ignoring everything else. 

As a final step, this bitset must be scanned for unset bits.

#### Memory
The size of the bitset required for this solution is 2^32/8n. 

#### Disk
Two passes. With buffered reading, the number of necessary disk operations is just like in solution one.

#### Remark
In this solution we are _hashing_ the content of the file.

### "Solution" three (Lucky Luke)

Well, this is not really a solution.
Just read through the file, and do a max/min search. At the end of reading, if min is greater than 0, or max is smaller than 2^32-1, we are lucky: 0 (or 2^32-1) is not present in the file.

#### Memory
Insignificant.

#### Disk
Just in all other solutions.

#### Remark
Isn't this called heuristic?

